package com.spike.giantdataanalysis.flink.example.streaming.operator

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger

/**
  * Demonstration of window trigger: determine window is ready to apply window function.
  */
object ExampleStreamingTrigger {
  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    val dataStream = stream(env)
      .keyBy(0)
      .timeWindow(Time.seconds(2))
      // 指定trigger
      .trigger(CountTrigger.of(5))
      .sum(0)
      .print()

    env.execute()
  }
}
