package com.spike.giantdataanalysis.akka.commons.futures

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import com.spike.giantdataanalysis.akka.commons._
import com.spike.giantdataanalysis.akka.commons.actor.PingPongActor
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.{Await, Future}

/**
  * 使用future获取actor的响应
  *
  * (1) 使用?询问(ask)actor对消息的响应
  * (2) scala.concurrent.Await.result()
  *
  * 包引用参见：http://alvinalexander.com/scala/scala-akka-actors-ask-examples-future-await-timeout-result
  * 包引用迁移参见：http://doc.akka.io/docs/akka/2.1.2/project/migration-guide-2.0.x-2.1.x.html
  *
  * 案例:
  * (1) pong actor
  * (2) future示例
  * (3) 组合future
  */
class FuturesSpec extends FunSpecLike with Matchers {
  val actorSystem = Akkas.actorSystem("FuturesSpecActorSystem")

  // 隐式的timeout
  implicit val timeout = Akkas.timeout(5, TimeUnit.SECONDS)
  val defaultDuration = Akkas.duration(1, TimeUnit.SECONDS)

  val pongActor = Akkas.actorOf(actorSystem, Props(classOf[PingPongActor], "Pong"), "PingPongActor")


  // 1 pong actor
  describe("Pong actor") {
    it("should respond with Pong") {

      // 询问(ask)actor对消息的响应
      val future: Future[Any] = pongActor ? "Ping"

      // 阻塞获取结果
      val result: String = Await.result(future.mapTo[String], defaultDuration)
      assert(result == "Pong")
    }

    it("should fail on unknown message") {
      val future = pongActor ? "unknown"

      // 期望的异常
      intercept[Exception] {
        Await.result(future.mapTo[String], defaultDuration)
      }
    }
  }

  def askPong(askedActor: ActorRef, message: String): Future[String] =
    (pongActor ? message).mapTo[String]

  // 2 future示例
  describe("FutureExamples") {
    import scala.concurrent.ExecutionContext.Implicits.global

    it("should print to console") {

      // 直接使用Future.onSuccess()
      (pongActor ? "Ping").onSuccess({
        case x: String => println("===> replied with: " + x)
      })

      // 等待完成
      Thread.sleep(100)
    }

    it("handle success cases") {

      // onSuccess
      askPong(pongActor, "Ping") onSuccess {
        case x: String => println("===> replied with: " + x)
      }

      // 转换结果: map
      askPong(pongActor, "Ping") map { x => x.charAt(0) } onSuccess {
        case x: Char => println("===> replied with: " + x)
      }

      // 异步转换结果
      val futureFuture: Future[Future[String]] =
        askPong(pongActor, "Ping") map { x => askPong(pongActor, x) }
      // 或者使用异步链: flatMap
      val futureFuture2: Future[String] =
        askPong(pongActor, "Ping") flatMap { x => askPong(pongActor, x) }

      Thread.sleep(100)
    }

    it("handle failure cases") {
      // onFailure
      askPong(pongActor, "causeError").onFailure {
        // 接受Throwable
        case e: Exception => println("===> Got exception")
      }

      // 恢复
      val recoverdFuture = askPong(pongActor, "causeError") recover {
        case t: Exception => "default"
      }

      // 异步恢复
      val asyncRecoverdFuture = askPong(pongActor, "causeError") recoverWith {
        case t: Exception => askPong(pongActor, "Ping")
      }

    }
  }

  // 3 组合future:
  describe("composing futures") {
    import scala.concurrent.ExecutionContext.Implicits.global

    it("chaining operation together") {
      val future = askPong(pongActor, "Ping").
        flatMap(x => askPong(pongActor, "Ping" + x)).
        recover({ case _: Exception => "There was an error" })

      future.onSuccess({ case x: String => println("===>" + x) })
    }

    it("combining futures") {
      val future1 = Future {
        4
      }
      val future2 = Future {
        5
      }

      // 这是flatMap的语法糖
      val futureAddtion: Future[Int] =
        for {
          result1 <- future1
          result2 <- future2
        } yield result1 + result2

      println(futureAddtion.collect[Unit]({
        {
          case x: Int => println("===>" + x)
        }
      }))
    }

    it("dealing with lists of futures") {
      // future的列表
      val listOfFutures: List[Future[String]] = List("Ping", "Ping", "causeError").map { x => askPong(pongActor, x) }

      // 转换为: 元素为列表的future
      val futureOfList: Future[List[String]] =
      // Future.sequence(listOfFutures)
      // 处理异常
        Future.sequence(listOfFutures map { future => future.recover({ case _: Exception => "" }) })

      futureOfList.onSuccess({ case x: List[String] => println("===>" + x) })
    }
  }

}

