package com.spike.giantdataanalysis.flink.example.streaming.connector

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig
import org.apache.flink.streaming.connectors.rabbitmq.{RMQSink, RMQSource}

object RabbitMQConfiguration {
  val host = "192.168.56.110"
  val port = 5672
  val userName = "guest"
  val password = "guest"
  val virtualHost = "/"
}

object ExampleStreamingRabbitMQConnector {
  def main(args: Array[String]): Unit = {

    val env = localStreamEnv
    env.enableCheckpointing(60000)

    val connectionConfig = new RMQConnectionConfig.Builder()
      .setHost(RabbitMQConfiguration.host)
      .setPort(RabbitMQConfiguration.port)
      .setUserName(RabbitMQConfiguration.userName)
      .setPassword(RabbitMQConfiguration.password)
      .setVirtualHost(RabbitMQConfiguration.virtualHost)
      .build

    val stream = env.addSource(new RMQSource[String](
      connectionConfig,
      "FlinkStreamingSourceQueue",
      false,
      new SimpleStringSchema))
      .setParallelism(1)

    stream.addSink(new RMQSink[String](
      connectionConfig,
      "FlinkStreamingTargetQueue",
      new SimpleStringSchema))

    env.execute()
  }
}
