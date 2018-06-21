package com.spike.giantdataanalysis.akka.core.server.actor

import akka.actor.{Actor, ActorRef, Status}
import akka.event.Logging

import com.spike.giantdataanalysis.akka.core.message.db._

/**
  * 内存数据库Actor
  *
  * 行为：
  * (1) 记录日志;
  * (2) 记录[[SetRequest]]消息值，便于后续检索.
  */
class DBActor extends Actor {

  // 模拟内存数据库
  val map = new collection.mutable.HashMap[String, Object]

  val logger = Logging(context.system, this) // 日志

  // type Receive = scala.PartialFunction[scala.Any, scala.Unit]
  override def receive: Receive = {
    case x: Connected =>
      sender() ! x

    case x: List[_] =>
      x.foreach {
        case SetRequest(key, value, senderRef) =>
          if (senderRef == ActorRef.noSender) handleSetRequest(key, value, sender())
          else handleSetRequest(key, value, senderRef)
        case GetRequest(key, senderRef) =>
          if (senderRef == ActorRef.noSender) handleGetRequest(key, sender())
          else handleGetRequest(key, senderRef)
      }

    case SetRequest(key, value, senderRef) =>
      if (senderRef == ActorRef.noSender) handleSetRequest(key, value, sender())
      else handleSetRequest(key, value, senderRef)

    case GetRequest(key, senderRef) =>
      if (senderRef == ActorRef.noSender) handleGetRequest(key, sender())
      else handleGetRequest(key, senderRef)

    case o =>
      logger.info("received unknown message: {}", o)
      sender() ! Status.Failure(new ClassNotFoundException)
  }

  private def handleSetRequest(key: String, value: Object, senderRef: ActorRef): Unit = {
    logger.info("received SetRequest - key: {}, value: {}", key, value)

    map.put(key, value)

    senderRef ! Status.Success
  }

  private def handleGetRequest(key: String, senderRef: ActorRef): Unit = {
    logger.info("received GetRequest - key: {}", key)

    val response: Option[Object] = map.get(key)

    response match {
      case Some(x) => senderRef ! x
      case None => senderRef ! Status.Failure(new KeyNotFoundException(key))
    }
  }

}