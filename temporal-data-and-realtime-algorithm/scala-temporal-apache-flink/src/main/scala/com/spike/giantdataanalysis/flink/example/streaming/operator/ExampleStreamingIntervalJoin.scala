package com.spike.giantdataanalysis.flink.example.streaming.operator

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

import scala.util.Random
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._

object ExampleStreamingIntervalJoin {
  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    val rnd = new Random()
    val stream1 = env.fromCollection(List.fill(100)((rnd.nextString(1), rnd.nextInt())))
    val stream2 = env.fromCollection(List.fill(103)((rnd.nextString(1), rnd.nextInt())))

    // A & B
    // a.timestamp + lowerBound <= b.timestamp <= a.timestamp + upperBound

    stream1.keyBy(_._1)
      .intervalJoin(stream2.keyBy(_._1))
      .between(Time.milliseconds(-2), Time.milliseconds(2))
      .process(new ProcessJoinFunction[(String, Int), (String, Int), String] {
        override def processElement(left: (String, Int),
                                    right: (String, Int),
                                    ctx: ProcessJoinFunction[(String, Int), (String, Int), String]#Context,
                                    out: Collector[String]): Unit = {

          out.collect(left + ", " + right)
        }
      })
      .print()

    env.execute()
  }
}
