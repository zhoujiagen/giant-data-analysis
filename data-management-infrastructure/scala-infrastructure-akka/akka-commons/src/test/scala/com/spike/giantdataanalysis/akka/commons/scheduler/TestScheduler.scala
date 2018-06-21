package com.spike.giantdataanalysis.akka.commons.scheduler

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.spike.giantdataanalysis.akka.commons.Akkas
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}


// WARNING
// The Akka scheduler is not designed for long-term scheduling (see
// akka-quartz-scheduler instead for this use case)
// nor is it to be used for highly precise firing of the events.


class SchedulerBDDSpec(testSystem: ActorSystem)
  extends TestKit(testSystem)
    with ImplicitSender
    with FunSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("SchedulerBDDAS"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(testSystem)
  }

  describe("Play with scheduler in actor system") {
    it("do some schedule tasks") {

      import testSystem.dispatcher

      val scheduler = testSystem.scheduler
      scheduler.scheduleOnce(
        Akkas.delay(100),
        testActor,
        "hello")
      expectMsg("hello")

      val cancellable = scheduler.schedule(
        Akkas.delay(10),
        Akkas.duration(200),
        testActor,
        "hello again")
      expectMsg("hello again")

      // cancel once tried
      TestKit.awaitCond(cancellable.cancel(), Akkas.duration(1000))
      expectNoMessage()
    }
  }

}