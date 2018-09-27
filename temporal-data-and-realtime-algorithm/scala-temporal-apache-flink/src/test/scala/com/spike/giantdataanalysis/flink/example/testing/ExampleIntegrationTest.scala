package com.spike.giantdataanalysis.flink.example.testing

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class MultiByTwoMapFunction extends MapFunction[Long, Long] {
  override def map(value: Long): Long =
    value * 2
}

// 集成测试
class TestMultiByTwoMapFunction extends FlatSpec with Matchers {

  "MultiByTwoMapFunction" should "mutli by 2" in {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.fromElements(1L, 2L, 3L)
      .map(new MultiByTwoMapFunction)
      .addSink(new CollectSink)

    env.execute()

    println(CollectSink.values)
    CollectSink.values should be(ListBuffer(2L, 4L, 6L))
  }
}


class CollectSink extends SinkFunction[Long] {
  override def invoke(value: Long): Unit = {
    synchronized {
      CollectSink.values += value
    }
  }
}

object CollectSink {
  val values: ListBuffer[Long] = ListBuffer()
}