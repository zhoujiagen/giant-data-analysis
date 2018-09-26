package com.spike.giantdataanalysis.flink.example.streaming

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._

case class WordWithCount(word: String, count: Int)

// demonstration for local debug
object ExampleStreamingLocal {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = localStreamEnv

    fromElements(env)

    env.execute()
  }

  private def fromElements(env: StreamExecutionEnvironment): Unit = {
    val text = env.fromElements("what is real is rational", "and what is rational is real")

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text.flatMap { w ⇒ w.split("\\s") }
      .map { w ⇒ WordWithCount(w, 1) }
      .keyBy("word")
      .sum("count")

    // launch the execution
    val result: JobExecutionResult = env.execute()
    println(s"JobID=${result.getJobID}")

    // mock sink
    import scala.collection.JavaConverters._
    val mockSink = DataStreamUtils.collect(windowCounts.javaStream).asScala
    println(s"Result=${mockSink.toList}")
  }

}
