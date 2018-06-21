package com.spike.giantdataanalysis.akka.commons.support


import akka.actor.Actor
import akka.testkit.{TestActorRef, TestProbe}


/**
  * Actor测试的fixture参数
  *
  * @param testActorRef TestActorRef[A]
  * @param testProbe    测试探针
  * @tparam A 待测试的Actor
  */
case class ActorTestFixture[A <: Actor](
                                         testActorRef: TestActorRef[A],
                                         testProbe: TestProbe)

//import org.scalatest._
//
//abstract class ActorSpec[A <: Actor](actorSystemName: String) extends fixture.FunSpec with Matchers {
//  implicit val actorSystem = ActorSystem(actorSystemName, ConfigFactory.empty())
//
//  type FixtureParam = ActorTestFixture[A]
//
//  override def withFixture(test: OneArgTest): Outcome = {
//    val actorRef = TestActorRef(new A())
//    val probe = TestProbe()
//    try {
//      test(ActorTestFixture(actorRef, probe))
//    } finally {
//      // do clean works
//    }
//  }
//}




