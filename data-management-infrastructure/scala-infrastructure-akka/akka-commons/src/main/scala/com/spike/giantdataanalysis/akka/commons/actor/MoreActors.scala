package com.spike.giantdataanalysis.akka.commons.actor

import akka.actor.{Actor, ActorSystem, DeadLetter, Props, Status}
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

  def props(response: String): Props = {
    Props(classOf[PingPongActor], response)
  }
}

/**
  * Dead Letter Listener Actor.
  */
class DeadLetterListener extends Actor {
  val logger = Logging(context.system, this)

  override def receive: Receive = {
    case d: DeadLetter => logger.info("{}", d)
  }
}

object DeadLetterListener {
  def props: Props = Props(classOf[DeadLetterListener])

  /**
    * 在ActorSystem中订阅.
    *
    * @param actorSystem
    * @param eventClass 事件类
    * @see [[akka.actor.DeadLetter]]
    * @see [[akka.actor.DeadLetterSuppression]]
    * @see [[akka.actor.AllDeadLetters]]
    *
    */
  def subscribe(actorSystem: ActorSystem, eventClass: Class[_]): Unit = {
    actorSystem.eventStream.subscribe(actorSystem.actorOf(props), eventClass)
  }
}


