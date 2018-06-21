package com.spike.giantdataanalysis.akka.core.server.actor


import akka.actor.Status
import akka.testkit.{TestActorRef, TestProbe}
import com.spike.giantdataanalysis.akka.commons.Akkas
import com.spike.giantdataanalysis.akka.core.message.db.{GetRequest, KeyNotFoundException, SetRequest}
import org.scalatest._

/**
  * [[DBActor]]的单元测试.
  */
class DBActorSpec extends FunSpec with Matchers {
  /// Well, it seems idea cannot run this file, then add manually or use sbt shell instead.

  // 忽略src/main/resources/application.conf中的配置
  implicit val actorSystem = Akkas.actorSystem("DBActorAS")


  val actorRef = TestActorRef(new DBActor)
  val testProbe = TestProbe()

  describe("DBActor") {
    describe("given SetRequest") {
      it("should place key/value into map") { //fixture => // 作为参数传入
        actorRef ! SetRequest("key", "value", testProbe.ref)

        // 获取底层的actor实例
        val dbActor = actorRef.underlyingActor
        dbActor.map.get("key") should equal(Some("value"))

        // 断言期望获取的消息
        testProbe.expectMsg(Status.Success)
      }
    }

    describe("non-existed key") {
      it("should not contain non-existed key") {
        actorRef ! SetRequest("key", "value", testProbe.ref)

        val db = actorRef.underlyingActor
        db.map.get("non-existed-key") should equal(None)

        actorRef ! GetRequest("non-existed-key", testProbe.ref)

        testProbe.expectMsg(Status.Success)
        testProbe.expectMsg(Status.Failure(new KeyNotFoundException("non-existed-key")))
      }
    }

    describe("given List[SetRequest]") {
      it("should place key/value pairs into map") {
        actorRef ! List(
          SetRequest("key", "value", testProbe.ref),
          SetRequest("key2", "value2", testProbe.ref))

        val dbActor = actorRef.underlyingActor
        dbActor.map.get("key") should equal(Some("value"))
        dbActor.map.get("key2") should equal(Some("value2"))

        testProbe.expectMsg(Status.Success)
        testProbe.expectMsg(Status.Success)
      }
    }

  }
}

