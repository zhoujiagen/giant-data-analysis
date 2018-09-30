package com.spike.giantdataanalysis.flink.example.application

import java.util.Date

import com.google.gson.Gson
import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema, SimpleStringSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig
import org.apache.flink.streaming.connectors.rabbitmq.{RMQSink, RMQSource}
import org.slf4j.LoggerFactory

import scala.util.Random

object RabbitMQConfiguration {
  val host = "192.168.56.110"
  val port = 5672
  val userName = "guest"
  val password = "guest"
  val virtualHost = "/"
}

case class SourceEvent(val id: Long,
                       val timestamp: Long,
                       val group: String,
                       val data: String)

object ExampleFlinkApplicationDataLoader {
  def main(args: Array[String]): Unit = {
    val streamEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val connectionConfig = new RMQConnectionConfig.Builder()
      .setHost(RabbitMQConfiguration.host)
      .setPort(RabbitMQConfiguration.port)
      .setUserName(RabbitMQConfiguration.userName)
      .setPassword(RabbitMQConfiguration.password)
      .setVirtualHost(RabbitMQConfiguration.virtualHost)
      .build

    val schema = new JsonStringSchema()
    val rnd = new Random()
    val groups = List("INFO", "WARN", "DEBUG", "ERROR", "FATAL")
    streamEnv.generateSequence(1L, 100000L).map(i => {
      Thread.sleep(200L) // delay
      new Gson().toJson(SourceEvent(i, new Date().getTime, groups(rnd.nextInt(10) % 5), "mocking payload"))
    }
    ).addSink(new RMQSink[String](connectionConfig, "FlinkStreamingSourceQueue", new SimpleStringSchema()))


    streamEnv.execute()
  }
}

object ExampleFlinkApplication {
  def main(args: Array[String]): Unit = {

    val streamEnv = StreamExecutionEnvironment.getExecutionEnvironment
    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //    streamEnv.setRestartStrategy(RestartStrategies.fixedDelayRestart(10, 1000L))
    //    val stateBackend: StateBackend = new FsStateBackend(new Path("file:///tmp/state2"), false)
    //    streamEnv.setStateBackend(stateBackend)
    //    streamEnv.setParallelism(10)
    //    streamEnv.setMaxParallelism(100)
    //    streamEnv.enableCheckpointing(5000L)
    //    streamEnv.setBufferTimeout(1000L)

    val connectionConfig = new RMQConnectionConfig.Builder()
      .setHost(RabbitMQConfiguration.host)
      .setPort(RabbitMQConfiguration.port)
      .setUserName(RabbitMQConfiguration.userName)
      .setPassword(RabbitMQConfiguration.password)
      .setVirtualHost(RabbitMQConfiguration.virtualHost)
      .build

    val schema = new JsonStringSchema()
    //new TypeInformationSerializationSchema[SourceEvent](createTypeInformation[SourceEvent], streamEnv.getConfig))
    val stream = streamEnv.addSource(
      new RMQSource[SourceEvent](connectionConfig, "FlinkStreamingSourceQueue", false, schema))
      .assignAscendingTimestamps(_.timestamp)
      .mapWith(event => (event.group, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(1))
      .sum(1)

    stream.print()

    streamEnv.execute()
  }
}


class JsonStringSchema extends DeserializationSchema[SourceEvent] with SerializationSchema[SourceEvent] {
  val LOG = LoggerFactory.getLogger(classOf[JsonStringSchema])

  override def deserialize(message: Array[Byte]): SourceEvent = {
    LOG.debug(s"deserialize: ${new String(message)}")
    JsonStringSchema.gson.fromJson(new String(message), classOf[SourceEvent])
  }

  override def isEndOfStream(nextElement: SourceEvent): Boolean = return false

  override def serialize(element: SourceEvent): Array[Byte] = {
    LOG.debug(s"serialize: ${element}")
    JsonStringSchema.gson.toJson(element).getBytes()
  }

  override def getProducedType: TypeInformation[SourceEvent] = createTypeInformation[SourceEvent]
}

object JsonStringSchema {
  val gson = new Gson()
}







