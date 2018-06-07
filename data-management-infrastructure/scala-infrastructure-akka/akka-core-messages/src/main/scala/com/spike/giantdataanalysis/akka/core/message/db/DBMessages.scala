package com.spike.giantdataanalysis.akka.core.message.db

import akka.actor.ActorRef


/** 键不存在，返回该失败 */
case class KeyNotFoundException(key: String) extends Exception


sealed trait Request

/** 设置键值，返回状态 */
case class SetRequest(key: String, value: Object, sender: ActorRef = ActorRef.noSender) extends Request

/** 返回键，如果存在的话 */
case class GetRequest(key: String, sender: ActorRef = ActorRef.noSender) extends Request

/** 作为Ping使用 */
case class Connected() extends Request
