package com.spike.giantdataanalysis.flink.example.streaming.operator

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.Random
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._

/**
  * Demonstration of window join
  */
object ExampleStreamingWindowJoin {

  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    val rnd = new Random()
    val stream1 = env.fromCollection(List.fill(100)((rnd.nextString(1), rnd.nextInt())))
    val stream2 = env.fromCollection(List.fill(103)((rnd.nextString(1), rnd.nextInt())))

    stream1.join(stream2)
      .where(_._1)
      .equalTo(_._1)
      //.window(TumblingEventTimeWindows.of(Time.seconds(2)))
      //.window(SlidingEventTimeWindows.of(Time.seconds(2)))
      .window(EventTimeSessionWindows.withGap(Time.minutes(10)))
      .apply {
        (t1, t2) => t1._2 + t2._2
      }
      .print()

    env.execute()
  }
}
