package com.spike.giantdataanalysis.akka.commons.persistence

import akka.event.Logging
import akka.persistence._
import akka.persistence.journal._
import akka.persistence.snapshot._
import com.spike.giantdataanalysis.commons.support.IDs

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.Try


object CURDMessages {

  /**
    * Domain
    * mocking outler persistence: [[content]] = IDs.sha512([[id]])
    */
  case class Domain(id: String, content: String)


  case object Shutdown


  sealed trait CRUDMessage

  //---------------------------------------------------------------------------
  // Commands
  //---------------------------------------------------------------------------
  sealed trait CRUDCommand extends CRUDMessage

  case class Create(cid: String, domain: Domain) extends CRUDCommand

  case class Read(cid: String, id: String) extends CRUDCommand

  case class Update(cid: String, domain: Domain) extends CRUDCommand

  case class Delete(cid: String, id: String) extends CRUDCommand

  //---------------------------------------------------------------------------
  // Events
  //---------------------------------------------------------------------------
  sealed trait CRUDEvent extends CRUDMessage

  case class Created(eid: String, domain: Domain) extends CRUDEvent

  case class Readed(eid: String, domain: Domain) extends CRUDEvent

  case class Updated(eid: String, sourceDomain: Domain, targetDomain: Domain) extends CRUDEvent

  case class Deleted(eid: String, domain: Domain) extends CRUDEvent

  case class SnapshotEvent(snapshotId: String) extends CRUDEvent

}


/// Technology Compatibility Kit:
/// "com.typesafe.akka" %% "akka-persistence-tck" % "2.5.12" % "test"

// Journal Plugin
class AJournalPlugin
  extends AsyncWriteJournal {
  override def asyncWriteMessages(messages: immutable.Seq[AtomicWrite]): Future[immutable.Seq[Try[Unit]]] = ???

  override def asyncDeleteMessagesTo(persistenceId: String,
                                     toSequenceNr: Long): Future[Unit] = ???

  override def asyncReplayMessages(persistenceId: String,
                                   fromSequenceNr: Long,
                                   toSequenceNr: Long,
                                   max: Long)
                                  (recoveryCallback: PersistentRepr => Unit): Future[Unit] = ???

  override def asyncReadHighestSequenceNr(persistenceId: String,
                                          fromSequenceNr: Long): Future[Long] = ???
}

// Snapshot Plugin
class ASnapshotPlugin extends SnapshotStore {
  override def loadAsync(persistenceId: String,
                         criteria: SnapshotSelectionCriteria): Future[Option[SelectedSnapshot]] = ???

  override def saveAsync(metadata: SnapshotMetadata,
                         snapshot: Any): Future[Unit] = ???

  override def deleteAsync(metadata: SnapshotMetadata): Future[Unit] = ???

  override def deleteAsync(persistenceId: String,
                           criteria: SnapshotSelectionCriteria): Future[Unit] = ???
}


class APersistenceActor(val id: String)
  extends PersistentActor
    with AtLeastOnceDelivery {

  val logger = Logging(this)
  val snapshotInterval = 1000

  import CURDMessages._


  //---------------------------------------------------------------------------
  // The State
  //---------------------------------------------------------------------------
  case class AState(events: List[CRUDEvent] = Nil) {
    def update(e: CRUDEvent): AState = {
      AState(e :: events)
    }

    def size(): Int = events.size
  }

  // internal state
  var state: AState = AState()

  //---------------------------------------------------------------------------
  // PersistentActor
  //---------------------------------------------------------------------------
  override def persistenceId: String = id

  // set recover executing conditions
  override def recovery: Recovery = {
    // close recover feature
    Recovery.none
    // when snapshot format is change, should relay all the events
    Recovery(fromSnapshot = SnapshotSelectionCriteria.None)
    // just use the latest snapshot
    Recovery(fromSnapshot = SnapshotSelectionCriteria.Latest)
  }

  // command handler
  override def receiveCommand: Receive = {
    case command: CRUDCommand =>
      logger.info(s"Handle Command: $command")
      command match {
        case c@Create(_, _) => create(c)
        case r@Read(_, _) => read(r)
        case u@Update(_, _) => update(u)
        case d@Delete(_, _) => delete(d)
        case _ =>
      }
      // persist the event: event, event handler
      // Successfully persisted events are internally sent back to the persistent actor
      //   as individual messages that trigger event handler executions.
      //  Incoming messages are stashed until the persist is completed.
      persist(SnapshotEvent(s"$command-${state.size()}")) { event =>
        state = state.update(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr != 0 && lastSequenceNr % snapshotInterval == 0) {
          // save the snapshot
          saveSnapshot(state)
        }
      }

    case Shutdown => context.stop(self) // safely close the PersistentActor

    case other: Any => logger.warning(s"Unexpected command message: $other")
  }

  private def create(command: Create): Unit = {
    state = state.update(Created(command.cid, Domain(command.domain.id, IDs.sha512(command.domain.id))))
  }

  private def read(command: Read): Unit = {
    state = state.update(Readed(command.cid, Domain(command.id, IDs.sha512(command.id))))
  }

  private def update(command: Update): Unit = {
    state = state.update(Updated(command.cid,
      sourceDomain = Domain(command.domain.id, IDs.sha512(command.domain.id)),
      targetDomain = Domain(command.domain.id, IDs.sha512(command.domain.id))))
  }

  private def delete(command: Delete): Unit = {
    state = state.update(Deleted(command.cid, Domain(command.id, IDs.sha512(command.id))))
  }


  // recovery handler
  override def receiveRecover: Receive = {
    case rc: RecoveryCompleted => logger.info("recover completed!")
    case e: CRUDEvent => state = state.update(e)
    case SnapshotOffer(_: SnapshotMetadata, snapshot: AState) => state = snapshot
    case other: Any => logger.warning(s"Unexpected recover message: $other")
  }

}

