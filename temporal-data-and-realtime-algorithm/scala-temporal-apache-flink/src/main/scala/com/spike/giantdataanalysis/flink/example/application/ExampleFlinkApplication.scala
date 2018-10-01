package com.spike.giantdataanalysis.flink.example.application

import java.util.Date

import com.google.gson.Gson
import com.rabbitmq.client.MessageProperties
import org.apache.flink.api.common.functions.{AggregateFunction, ReduceFunction}
import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema, SimpleStringSchema, TypeInformationSerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig
import org.apache.flink.streaming.connectors.rabbitmq.{RMQSink, RMQSource}
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory

import scala.util.Random

object RabbitMQConfiguration {
  val host = "127.0.0.1"
  val port = 5672
  val userName = "guest"
  val password = "guest"
  val virtualHost = "/"
  val sourceQueueName = "FlinkStreamingSourceQueue"
  val targetQueueName = "FlinkStreamingTargetQueue"
}

case class SourceEvent(val id: Long,
                       val timestamp: Long,
                       val group: String,
                       val data: String)

case class SinkEvent(val group: String,
                     val windowStart: Long,
                     val count: Long)

/** log data generator. */
object ExampleFlinkApplicationDataLoader {
  def main(args: Array[String]): Unit = {

    import com.rabbitmq.client.ConnectionFactory
    val factory = new ConnectionFactory
    factory.setHost(RabbitMQConfiguration.host)
    factory.setPort(RabbitMQConfiguration.port)
    factory.setUsername(RabbitMQConfiguration.userName)
    factory.setPassword(RabbitMQConfiguration.password)
    factory.setVirtualHost(RabbitMQConfiguration.virtualHost)
    val connection = factory.newConnection
    val channel = connection.createChannel
    channel.queueDeclarePassive(RabbitMQConfiguration.sourceQueueName)

    val gson = new Gson()
    val rnd = new Random()
    val groups = List("INFO", "WARN", "DEBUG", "ERROR", "FATAL")
    for (i <- 1L to 10000L) {
      Thread.sleep(20L) // delay
      val message = gson.toJson(SourceEvent(i, new Date().getTime, groups(rnd.nextInt(10) % 5), "mocking payload"))
      println(s"Send message: $message")
      channel.basicPublish("", RabbitMQConfiguration.sourceQueueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
        message.getBytes)
    }

    channel.close()
    connection.close()
  }
}

/** aggregate log count per group per 5 seconds. */
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

    val stream = streamEnv.addSource(
      new RMQSource[SourceEvent](connectionConfig, RabbitMQConfiguration.sourceQueueName, false, new SourceEventSchema()))
      .assignAscendingTimestamps(_.timestamp)
      .map(event => (event.group, 1L))
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      //.sum(1) // DEBUG
      .aggregate(new ToSinkAggregateFunction(), new ToSinkProcessWindowFunction())

    //stream.print() // DEBUG
    stream.addSink(new RMQSink[SinkEvent](connectionConfig, RabbitMQConfiguration.targetQueueName, new SinkEventSchema()))

    streamEnv.execute()
  }
}

class ToSinkAggregateFunction extends AggregateFunction[(String, Long), Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: (String, Long), accumulator: Long): Long = accumulator + value._2

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class ToSinkProcessWindowFunction extends ProcessWindowFunction[Long, SinkEvent, String, TimeWindow] {
  override def process(key: String, context: Context, elements: Iterable[Long], out: Collector[SinkEvent]): Unit = {
    out.collect(SinkEvent(key, context.window.getStart, elements.sum))
  }
}

class SourceEventSchema extends DeserializationSchema[SourceEvent] with SerializationSchema[SourceEvent] {
  val LOG = LoggerFactory.getLogger(classOf[SourceEventSchema])

  override def deserialize(message: Array[Byte]): SourceEvent = {
    LOG.debug(s"deserialize: ${new String(message)}")
    JsonStringSchema.gson.fromJson(new String(message), classOf[SourceEvent])
  }

  override def isEndOfStream(nextElement: SourceEvent): Boolean = false

  override def serialize(element: SourceEvent): Array[Byte] = {
    LOG.debug(s"serialize: $element")
    JsonStringSchema.gson.toJson(element).getBytes()
  }

  override def getProducedType: TypeInformation[SourceEvent] = createTypeInformation[SourceEvent]
}

class SinkEventSchema extends DeserializationSchema[SinkEvent] with SerializationSchema[SinkEvent] {
  val LOG = LoggerFactory.getLogger(classOf[SinkEventSchema])

  override def deserialize(message: Array[Byte]): SinkEvent = {
    LOG.debug(s"deserialize: ${new String(message)}")
    JsonStringSchema.gson.fromJson(new String(message), classOf[SinkEvent])
  }

  override def isEndOfStream(nextElement: SinkEvent): Boolean = false

  override def serialize(element: SinkEvent): Array[Byte] = {
    LOG.debug(s"serialize: $element")
    JsonStringSchema.gson.toJson(element).getBytes()
  }

  override def getProducedType: TypeInformation[SinkEvent] = createTypeInformation[SinkEvent]
}

object JsonStringSchema {
  val gson = new Gson()
}







