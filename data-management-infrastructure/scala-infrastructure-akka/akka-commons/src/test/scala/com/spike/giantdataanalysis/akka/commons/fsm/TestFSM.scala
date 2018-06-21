package com.spike.giantdataanalysis.akka.commons.fsm

import java.util.concurrent.TimeUnit

import akka.actor.FSM.Reason
import akka.actor.{ActorRef, ActorSystem, FSM, LoggingFSM, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.spike.giantdataanalysis.akka.commons.Akkas
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSpec, FunSpecLike, Matchers}
import org.slf4j.LoggerFactory

import scala.collection.immutable


object BuncherMessages {

  // received events
  final case class SetTarget(ref: ActorRef)

  final case class Queue(obj: Any)

  case object Flush

  // sent events
  final case class Batch(obj: immutable.Seq[Any])


  // states
  sealed trait State

  case object Idle extends State

  case object Active extends State

  // data
  sealed trait Data

  case object Uninitialized extends Data

  final case class Todo(target: ActorRef, queue: immutable.Seq[Any]) extends Data

}

import com.spike.giantdataanalysis.akka.commons.fsm.BuncherMessages._

/**
  * FSM示例.
  */
class Buncher extends FSM[State, Data] {
  // S0
  startWith(Idle, Uninitialized)


  // when 'State' encounter 'event', then do something or 'state transition'
  when(Idle) {
    case FSM.Event(SetTarget(actorRef), Uninitialized) =>
      stay using Todo(actorRef, Vector.empty)
  }

  when(Active, stateTimeout = Akkas.duration(1, TimeUnit.SECONDS)) {
    case FSM.Event(Flush | StateTimeout, t: Todo) =>
      goto(Idle) using t.copy(queue = Vector.empty)

  }

  // exception event handler
  whenUnhandled {
    case FSM.Event(Queue(obj), t@Todo(_, v)) ⇒
      goto(Active) using t.copy(queue = v :+ obj)

    case FSM.Event(event, stateData) ⇒
      log.warning("received unhandled request {} in state {}/{}", event, stateName, stateData)
      stay
  }


  // wen can have multiple transition handler
  onTransition {
    case Active -> Idle =>
      // use state data
      stateData match {
        case Todo(actorRef, queue) => actorRef ! queue
        case _ => // do nothing
      }
  }

  onTransition {
    case Active -> Idle => log.debug("Transition: Active -> Idle")
    case Idle -> Active => log.debug("Transition: Idle -> Active")
  }


  // a termination hook
  onTermination {
    case StopEvent(reason: Reason, currentState: BuncherMessages.State, stateData: BuncherMessages.Data) =>
      val reasonRepr =
        reason match {
          case FSM.Shutdown => "shutdown"
          case FSM.Normal => "normal"
          case FSM.Failure(cause) => cause
        }
      log.info("reason: {}, currentState: {}, currentDate: {}", reasonRepr, currentState, stateData)
  }


  // start S0
  initialize()
}

object Buncher {
  def props(): Props =
    Akkas.props[Buncher]
}

class BuncherSpec extends FunSpec with Matchers with BeforeAndAfterAll {

  val logger = LoggerFactory.getLogger(this.getClass)

  var as: ActorSystem = null
  var buncherActorRef: ActorRef = null

  override def beforeAll(): Unit = {
    val configString =
      """
        |akka {
        |   loggers = ["akka.event.slf4j.Slf4jLogger"]
        |   loglevel = "DEBUG"
        |   logging-filter = "akka.event.slf4j.Slf4jLoggingFilter"
        |   log-config-on-start = off
        |   stdout-loglevel = "OFF"
        |
        |   actor {
        |       debug {
        |           receive = on
        |           autoreceive = on
        |           lifecycle = on
        |           fsm = on
        |           event-stream = on
        |           unhandled = on
        |           router-misconfiguration = on
        |    }
        |
        |   }
        |}
      """.stripMargin
    logger.info(configString)

    val config = ConfigFactory.parseString(configString)
    as = Akkas.actorSystem("TestBuncherFSMAS", config)
    buncherActorRef = Akkas.actorOf(as, Buncher.props(), "Buncher")
  }

  override def afterAll(): Unit = {
    Akkas.stop(as, buncherActorRef)

    import scala.concurrent.ExecutionContext.Implicits._
    Akkas.terminate(as).onComplete(println)
  }

  describe("play with FSM") {
    it("should output state transition log and unhandled log") {
      buncherActorRef ! SetTarget(buncherActorRef) // send to itself
      Thread.sleep(500L)
      buncherActorRef ! Queue(42)
      buncherActorRef ! Queue(43)
      Thread.sleep(500L)
      buncherActorRef ! Flush
    }
  }

}

class DummyExampleFSM extends LoggingFSM[State, Data] {
  override def logDepth: Int = 12

  startWith(Idle, Uninitialized)

  when(Idle) {
    case FSM.Event(SetTarget(actorRef), Uninitialized) =>
      stay using Todo(actorRef, Vector.empty)
  }

  onTermination {
    // call 'getLog' to get log stack
    case StopEvent(reason, currentState, stateData) =>
      log.info("reason: {}, state: {}, data: {}, trace: {}",
        reason, currentState, stateData, getLog.mkString("\n\t"))
  }

  initialize()
}

object DummyExampleFSM {
  def props: Props = Props(classOf[DummyExampleFSM])
}

// BDD with LoggingFSM
class BuncherBDDSpec(testSystem: ActorSystem)
  extends TestKit(testSystem)
    with ImplicitSender
    with FunSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("PingPongActorBDDAS"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(testSystem)
  }


  describe("Play with LoggingFSM") {
    it("should output depth trace when terminated") {

      val actorRef = testSystem.actorOf(DummyExampleFSM.props)
      actorRef ! SetTarget(actorRef)
      actorRef ! "DUMMY"
    }
  }

}