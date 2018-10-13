package com.spike.giantdataanalysis.flink.example.streaming.time


import java.util.Date

import com.google.common.hash.Hashing
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.{AssignerWithPeriodicWatermarks, AssignerWithPunctuatedWatermarks}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.Random

object ExampleStreamingGenTimestampAndWatermark {

  def main(args: Array[String]): Unit = {
    val env = localStreamEnv // 事件时间特征
    println(env.getStreamTimeCharacteristic)

    env.getConfig.setAutoWatermarkInterval(5000L)

    println(useTimestampAssigner(env))
  }

  def useSourceFunction(env: StreamExecutionEnvironment): Unit = {


    val source = new DummySourceFunction // 使用带时间戳和水位线的数据源函数
    env.addSource(source).print()

    env.execute()
  }

  def useTimestampAssigner(env: StreamExecutionEnvironment): Unit = {

    val source = new DummyLogEventSourceFunction
    val watermarkAssigner = new DummyAPeriodicWatermarks
    env.addSource(source)
      .filter(_.severity == 2)
      .assignTimestampsAndWatermarks(watermarkAssigner) // 指派时间戳和水位线
      .keyBy(_.group)
      .timeWindow(Time.seconds(5))
      .reduce((e1, e2) => e1.add(e2))
      .print()

    env.execute()
  }
}

//---------------------------------------------------------------------------
// source function with timestamp and watermark
//---------------------------------------------------------------------------


case class DummyEvent(id: String, timestamp: Long, detail: String, watermark: Option[Long]) {
  def hasWatermark(): Boolean = watermark.isDefined
}

object DummyEventFactory {
  var eventCount = 0

  def nextEvent(): DummyEvent = {
    eventCount = eventCount + 1
    val timestamp = new Date().getTime
    val id = Hashing.md5().hashLong(timestamp).toString

    DummyEvent(id, timestamp, "e-" + timestamp,
      if (eventCount % 100 == 0) Some(timestamp - 5000L) else None)
  }
}


class DummySourceFunction extends SourceFunction[DummyEvent] {
  @volatile var running: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[DummyEvent]): Unit = {
    while (running) {
      val next = DummyEventFactory.nextEvent()
      ctx.collectWithTimestamp(next, next.timestamp) // 收集事件和时间戳

      if (next.hasWatermark()) {
        ctx.emitWatermark(new Watermark(next.watermark.get)) // 提交水位线
      }
    }
  }

  override def cancel(): Unit = running = false
}


//---------------------------------------------------------------------------
// timestamp assigner and watermark generator
//---------------------------------------------------------------------------
case class DummyLogEvent(group: String, severity: Int, message: String, createTime: Date = new Date(), isWaterMarker: Boolean = false) {
  def add(other: DummyLogEvent): DummyLogEvent = {
    DummyLogEvent(group, severity, message.concat(other.message))
  }
}

object DummyLogEvent {
  val eventGroups = List("G1", "G2", "G3")
  val severity = List(0, 1, 2, 3)
}

class DummyLogEventSourceFunction extends SourceFunction[DummyLogEvent] {
  @volatile var running: Boolean = true
  val rnd = new Random()
  var eventCount = 0

  override def run(ctx: SourceFunction.SourceContext[DummyLogEvent]): Unit = {

    while (running) {
      eventCount = eventCount + 1

      ctx.collect(DummyLogEvent(
        DummyLogEvent.eventGroups(rnd.nextInt(DummyLogEvent.eventGroups.length)),
        DummyLogEvent.severity(rnd.nextInt(DummyLogEvent.severity.length)),
        Hashing.md5().hashLong(new Date().getTime).toString,
        isWaterMarker = eventCount % 100 == 0)) // 收集事件
    }
  }

  override def cancel(): Unit = running = false
}

// AssignerWithPeriodicWatermarks
// 周期性的生成水位线: 基于流元素, 或者基于处理时间
// 配置: org.apache.flink.api.common.ExecutionConfig.setAutoWatermarkInterval
class DummyAPeriodicWatermarks extends AssignerWithPeriodicWatermarks[DummyLogEvent] {
  val maxOutOfOrderness = 3500L // 3.5 seconds
  var currentMaxTimestamp: Long = _

  override def extractTimestamp(element: DummyLogEvent, previousElementTimestamp: Long): Long = {
    val timestamp = element.createTime.getTime
    currentMaxTimestamp = math.max(timestamp, currentMaxTimestamp)
    timestamp
  }

  // 每次Interval时间已过被调用, 返回的水位线非空以及比之前大时提交水位线
  override def getCurrentWatermark: Watermark = {
    new Watermark(currentMaxTimestamp - maxOutOfOrderness)
  }
}

// AssignerWithPunctuatedWatermarks
// 特定事件的发生指明可以生成新的水位线
class DummyAPunctuatedWatermarks extends AssignerWithPunctuatedWatermarks[DummyLogEvent] {
  // 先调用
  override def extractTimestamp(element: DummyLogEvent, previousElementTimestamp: Long): Long =
    element.createTime.getTime

  // 再调用
  // extractedTimestamp由调用extractTimestamp(...)获得
  // 返回的水位线非空以及比之前大时提交水位线
  override def checkAndGetNextWatermark(lastElement: DummyLogEvent, extractedTimestamp: Long): Watermark =
    if (lastElement.isWaterMarker) new Watermark(extractedTimestamp) else null
}


