package com.spike.giantdataanalysis.akka.commons.actor


import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.spike.giantdataanalysis.akka.commons._
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


//---------------------------------------------------------------------------
// Unit Test
//---------------------------------------------------------------------------

class PingPongActorSpec extends FunSpec with Matchers {

  //---------------------------------------------------------------------------
  // Fields
  //---------------------------------------------------------------------------
  implicit val actorSystem = Akkas.actorSystem("PingPongActorAS")

  implicit val defaultTimeout: akka.util.Timeout = Akkas.timeout(2, TimeUnit.SECONDS)

  val actorRef = TestActorRef(new PingPongActor())
  val probe = TestProbe()


  //---------------------------------------------------------------------------
  // Helper Method
  //---------------------------------------------------------------------------
  def askPong(askedActor: ActorRef, message: String): Future[String] =
    (askedActor ? message).mapTo[String]


  //---------------------------------------------------------------------------
  // Tests
  //---------------------------------------------------------------------------
  // 1 pong actor
  describe("Pong actor") {
    it("should respond with Pong") {

      // 询问(ask)actor对消息的响应
      val future: Future[Any] = actorRef ? "Ping" // 使用隐式的timeout

      // 阻塞获取结果
      val result: String = Await.result(future.mapTo[String], 1 second)
      assert(result == "Pong")
    }

    it("should fail on unknown message") {
      val future = actorRef ? "unknown"

      // 期望的异常
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

  // 2 future示例
  describe("FutureExamples") {
    import scala.concurrent.ExecutionContext.Implicits.global

    it("should print to console") {

      // 直接使用Future.onSuccess()
      (actorRef ? "Ping").onSuccess({
        case x: String ⇒ println("===> replied with: " + x)
      })

      // 等待完成
      Thread.sleep(100)
    }

    it("handle success cases") {

      // onSuccess
      askPong(actorRef, "Ping").onSuccess {
        case x: String ⇒ println("===> replied with: " + x)
      }

      // 转换结果
      askPong(actorRef, "Ping").map { x ⇒ x.charAt(0) } onSuccess {
        case x: Char ⇒ println("===> replied with: " + x)
      }

      // 异步转换结果
      val futureFuture: Future[Future[String]] =
        askPong(actorRef, "Ping").map { x ⇒ askPong(actorRef, x) }
      // 或者使用异步链
      val futureFuture2: Future[String] =
        askPong(actorRef, "Ping").flatMap { x ⇒ askPong(actorRef, x) }

      Thread.sleep(100)
    }

    it("handle failure cases") {
      // onFailure
      askPong(actorRef, "causeError").onFailure {
        // 接受Throwable
        case e: Exception ⇒ println("===> Got exception")
      }

      // 恢复
      val recoverdFuture = askPong(actorRef, "causeError").recover {
        case t: Exception ⇒ "default"
      }

      // 异步恢复
      val asyncRecoverdFuture = askPong(actorRef, "causeError").recoverWith {
        case t: Exception ⇒ askPong(actorRef, "Ping")
      }

    }
  }

}


//---------------------------------------------------------------------------
// BDD Test
//---------------------------------------------------------------------------

class PingPongActorBDDSpec(testSystem: ActorSystem)
  extends TestKit(testSystem)
    with ImplicitSender
    with FunSpecLike with Matchers with BeforeAndAfterAll {

  // NOT WIRKING
  //import scala.collection.JavaConverters._
  //  def this() = this(ActorSystem("PingPongActorBDDAS",
  //    ConfigFactory.parseMap(Map("akka.test.default-timeout" -> 2000).asJava)))

  def this() = this(ActorSystem("PingPongActorBDDAS"))

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(testSystem)
  }

  describe("Pong actor") {
    it("should respond with Pong") {
      val actorRef = testSystem.actorOf(Props(classOf[PingPongActor], "Pong"))
      actorRef ! "Ping"
      expectMsg("Pong")

      expectMsg("Pong!") // let the test fail
    }
  }
}


