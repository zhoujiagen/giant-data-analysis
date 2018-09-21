package com.spike.giantdataanalysis.flink.example.streaming

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object ExampleStreamingHelloWorld {
  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    // source : socket
    val textStream = env.socketTextStream("localhost", 9999)
    val wordCountStream = textStream
      .flatMap {
        _.toLowerCase.split("\\W+").filter {
          _.nonEmpty
        }
      }.map {
      (_, 1)
    }
      .keyBy(0)
      .timeWindow(Time.seconds(2))
      .sum(1)

    // target: console
    wordCountStream.print()

    env.execute()
  }
}
