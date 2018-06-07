package com.spike.giantdataanalysis.akka.commons.logging

import akka.actor.{Actor, Props}
import akka.event.Logging
import com.spike.giantdataanalysis.akka.commons.Akkas
import com.typesafe.config.ConfigFactory
import org.scalatest.{FunSpec, Matchers}
import org.slf4j.LoggerFactory

class _Actor extends Actor {
  val log = Logging.getLogger(context.system.eventStream, this)

  override def receive: Receive = {
    case "ok" => log.info("OK")
    case "stop" => log.info("STOP")
    case msg => log.warning(s"Unexpected message: $msg")
  }
}

object _Actor {
  def props(): Props = Props(classOf[_Actor])
}

class TestLoggingSlf4jSpec extends FunSpec with Matchers {
  val logger = LoggerFactory.getLogger(this.getClass)

  describe("play with logback") {
    it("should run with logging") {
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
      val as = Akkas.actorSystem("TestBuncherFSMAS", config)
      val actorRef = Akkas.actorOf(as, _Actor.props(), "_Actor")


      actorRef ! "ok"
      actorRef ! "hello"
      actorRef ! "ok"
      actorRef ! "stop"

      import scala.concurrent.ExecutionContext.Implicits._
      Akkas.terminate(as).onComplete(println)
    }
  }
}
