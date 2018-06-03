package com.spike.giantdataanalysis.akka.commons.actor

import akka.actor.{Actor, Status}
import akka.event.Logging

/**
  * Actor for debug
  */
class DebugActor extends Actor {
  val logger = Logging(context.system, this)

  override def receive: Receive = {
    case msg => logger.info("Receive message: {}", msg)
  }
}

/**
  * Echo Actor
  */
class EchoActor extends Actor {
  override def receive: Receive = {
    case msg => sender() ! msg
  }
}


/**
  * Ping-Pong Actor
  *
  * @param response 响应内容
  */
class PingPongActor(val response: String = "Pong") extends Actor {
  val logger = Logging(context.system, this)

  override def receive: Receive = {
    case "Ping" =>
      sender() ! response
    case _ =>
      sender() ! Status.Failure(new Exception("unknown message"))
  }
}

object PingPongActor {

  import akka.actor.Props

  def props(response: String): Props = {
    Props(classOf[PingPongActor], response)
  }
}

