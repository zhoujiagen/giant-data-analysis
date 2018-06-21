package com.spike.giantdataanalysis.akka.commons.persistence

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props, SupervisorStrategy}
import akka.event.Logging
import akka.persistence.journal.leveldb.{SharedLeveldbJournal, SharedLeveldbStore}
import akka.persistence.{PersistentActor, SaveSnapshotFailure, SaveSnapshotSuccess, SnapshotOffer}
import akka.testkit.{ImplicitSender, TestKit}
import com.spike.giantdataanalysis.commons.support.IDs
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}


//---------------------------------------------------------------------------
// Events
//---------------------------------------------------------------------------
object Messages {

  case class Cmd(data: String)

  sealed trait ManagementCmd

  case object QueryCmd extends ManagementCmd

  case object EvilCmd extends ManagementCmd

  case class Evt(data: String)


}

object States {

  import Messages._

  case class EventStore(events: List[Evt] = Nil) {
    def update(event: Evt): EventStore = this.copy(event :: events)

    def size: Int = events.size

    override def toString: String = events.reverse.toString
  }

}


//---------------------------------------------------------------------------
// Persistence Actor implementation
//---------------------------------------------------------------------------

class EventPersistenceActor extends PersistentActor {
  val logger = Logging(context.system, this)


  override val supervisorStrategy = SupervisorStrategy.defaultStrategy

  import Messages._
  import States._

  // 内部状态
  private[this] var state = new EventStore
  val snapshotBatchSize = 10

  private[this] def updateState(event: Evt): Unit = state = state.update(event)

  override def persistenceId: String = "EventPersistence-" + IDs.UUID()


  override def receiveCommand: Receive = {
    case Cmd(data) =>
      persist(Evt(s"${data}-${state.size}")) { event =>
        updateState(event)
        // send to event stakeholders
        context.system.eventStream.publish(event)
        // snapshot internal state: faster recover
        if (lastSequenceNr != 0 && lastSequenceNr % snapshotBatchSize == 0) {
          logger.info("Taking snapshot...")
          saveSnapshot(state)
        }
      }

    // snapshot notification
    case SaveSnapshotSuccess(metadata) => logger.info("Snapshot success: {}", metadata)
    case SaveSnapshotFailure(metadata, cause) => logger.error(s"Snapshot failed: ${metadata}", cause)

    // management command
    case QueryCmd => logger.info("State={}", state)
    case EvilCmd => throw new Exception(EvilCmd.toString)
  }

  /// persist edge case handler

  override def onPersistFailure(cause: Throwable, event: Any, seqNr: Long): Unit = {
    super.onPersistFailure(cause, event, seqNr)
  }

  override def onPersistRejected(cause: Throwable, event: Any, seqNr: Long): Unit = {
    super.onPersistRejected(cause, event, seqNr)
  }


  override def receiveRecover: Receive = {
    // persisted msg replay
    case evt@Evt(_) => {
      logger.info("Recovering: {}", evt)
      updateState(evt)
    }
    // snapshot offer
    case SnapshotOffer(metadata, snapshot: EventStore) => {
      logger.info("Recovering: metadata={}, snapshot={}", metadata, snapshot)
      state = snapshot
    }
  }

  /// recover edge case handler
  override def onRecoveryFailure(cause: Throwable, event: Option[Any]): Unit = {
    super.onRecoveryFailure(cause, event)
  }

}

object EventPersistenceActor {
  def props: Props = Props(classOf[EventPersistenceActor])
}


//---------------------------------------------------------------------------
// BDD Test
//---------------------------------------------------------------------------
class PersistenceActorBDDSpec(testSystem: ActorSystem)
  extends TestKit(testSystem)
    with ImplicitSender
    with FunSpecLike with Matchers with BeforeAndAfterAll {

  import Messages._
  import States._

  var persistenceActorRef: ActorSelection = null

  def this() = this(ActorSystem("AS"))

  override def beforeAll(): Unit = {

    // start shared level db journal actor
    val leveldbStoreActorRef = testSystem.actorOf(Props[SharedLeveldbStore], "store")
    SharedLeveldbJournal.setStore(leveldbStoreActorRef, testSystem)

    val name = "pt" // + IDs.UUID()
    testSystem.actorOf(EventPersistenceActor.props, name)
    persistenceActorRef = testSystem.actorSelection("/user/" + name)
  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(testSystem)
  }


  describe("Play with persistence actor") {
    it("should save snapshot") {
      for (i <- 1 to 20) {
        persistenceActorRef ! Cmd(IDs.UUID())
        if (i % 10 == 0) {
          persistenceActorRef ! QueryCmd
        }
      }

      // hold on a second
      Thread.sleep(500L)

      // trigger actor restart
      persistenceActorRef ! EvilCmd
      // wait for restart
      Thread.sleep(5000L)

      // TODO(zhoujiagen) well it's: State=List() ???
      // check whether work as usual
      persistenceActorRef ! QueryCmd
      // wait for check
      Thread.sleep(5000L)
    }
  }


}
