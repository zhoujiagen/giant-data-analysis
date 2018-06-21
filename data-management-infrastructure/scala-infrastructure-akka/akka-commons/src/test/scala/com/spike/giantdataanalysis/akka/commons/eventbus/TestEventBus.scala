package com.spike.giantdataanalysis.akka.commons.eventbus

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event._
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Subclassification
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}

/**
  * 自定义事件总线实现.
  *
  * @see [[akka.event.EventBus]]
  * @see [[akka.event.LookupClassification]]
  * @see [[akka.event.SubchannelClassification]]
  * @see [[akka.event.ScanningClassification]]
  */

//---------------------------------------------------------------------------
// Message Protocol
//---------------------------------------------------------------------------
object Messages {

  final case class MessageEvelope(topic: String, payload: Any)


  sealed trait Music {
    def artist: String
  }

  case class Jazz(artist: String) extends Music

  case class Electronic(artist: String) extends Music

}

import com.spike.giantdataanalysis.akka.commons.eventbus.Messages._


//---------------------------------------------------------------------------
// EventBus with LookupClassification
//---------------------------------------------------------------------------
class LookupClassificationEventBus extends EventBus with LookupClassification {

  override type Event = MessageEvelope
  override type Classifier = String
  override type Subscriber = ActorRef

  // number of classifiers
  override protected def mapSize(): Int = 128

  override protected def compareSubscribers(a: Subscriber, b: Subscriber): Int = a.compareTo(b)

  override protected def classify(event: Event): Classifier = event.topic

  override protected def publish(event: Event, subscriber: Subscriber): Unit =
  // strip the 'topic'
    subscriber ! event.payload
}

//---------------------------------------------------------------------------
// EventBus with SubchannelClassification
//---------------------------------------------------------------------------
class SubchannelClassificationEventBus extends EventBus with SubchannelClassification {
  override type Event = MessageEvelope
  override type Classifier = String
  override type Subscriber = ActorRef

  class StartWithClassification extends Subclassification[String] {
    override def isEqual(x: String, y: String): Boolean = x == y

    override def isSubclass(x: String, y: String): Boolean = x startsWith y
  }


  override protected implicit def subclassification: Subclassification[String] = new StartWithClassification

  override protected def classify(event: MessageEvelope): String = event.topic

  override protected def publish(event: MessageEvelope, subscriber: ActorRef): Unit = subscriber ! event.payload
}


//---------------------------------------------------------------------------
// EventBus with ScanningClassification
//---------------------------------------------------------------------------
class ScanningClassificationEventBus extends EventBus with ScanningClassification {
  override type Event = String
  override type Classifier = Int
  override type Subscriber = ActorRef

  override protected def compareClassifiers(a: Int, b: Int): Int = a.compareTo(b)

  override protected def compareSubscribers(a: ActorRef, b: ActorRef): Int = a.compareTo(b)

  override protected def matches(classifier: Int, event: String): Boolean = event.length <= classifier

  override protected def publish(event: String, subscriber: ActorRef): Unit = subscriber ! event
}

//---------------------------------------------------------------------------
// EventStream in ActorSystem
//---------------------------------------------------------------------------

class MusicListener extends Actor {
  val logger = Logging(context.system, this)

  override def receive: Receive = {
    case jazz: Jazz => logger.info("{} receive {}", self, jazz)
    case electronic: Electronic => logger.info("{} receive {}", self, electronic)
  }
}

object MusicListener {
  def props: Props = Props(classOf[MusicListener])
}

//---------------------------------------------------------------------------
// BDD Spec
//---------------------------------------------------------------------------

class EventBusBDDSpec(testSystem: ActorSystem)
  extends TestKit(testSystem)
    with ImplicitSender
    with FunSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("EventBusBDDAS"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(testSystem)
  }

  describe("lookup classification on event bus") {
    it("should respond when published") {

      val eventBus = new LookupClassificationEventBus
      eventBus.subscribe(testActor, "greetings") // SUBSCRIBE

      eventBus.publish(MessageEvelope("time", System.currentTimeMillis())) // PUBLISH
      eventBus.publish(MessageEvelope("greetings", "hello"))

      expectMsg("hello")
    }
  }


  describe("sub channel classification on event bus") {
    it("should respond when published") {
      val eventBus = new SubchannelClassificationEventBus
      eventBus.subscribe(testActor, "abc")

      eventBus.publish(MessageEvelope("xyz", "xyz"))
      eventBus.publish(MessageEvelope("abc", "abc"))
      expectMsg("abc")
      eventBus.publish(MessageEvelope("bc", "bc"))
      eventBus.publish(MessageEvelope("abcd", "abcd"))
      expectMsg("abcd")
    }
  }

  describe("scanning classification on event bus") {
    it("should respond when published") {
      val eventBus = new ScanningClassificationEventBus

      eventBus.subscribe(testActor, 3)
      eventBus.publish("abcxyz")
      eventBus.publish("abc")
      expectMsg("abc")
    }
  }

  describe("event stream in actor system with music listener") {
    it("should listened some music") {
      val jazzListener = testSystem.actorOf(MusicListener.props)
      val musicListener = testSystem.actorOf(MusicListener.props)

      val eventStream = testSystem.eventStream
      eventStream.subscribe(jazzListener, classOf[Jazz])
      eventStream.subscribe(musicListener, classOf[Music])

      Thread.sleep(500L)

      eventStream.publish(Electronic("Parov Stelar"))
      eventStream.publish(Jazz("Sonny Rollins"))

      Thread.sleep(500L)
    }
  }

}