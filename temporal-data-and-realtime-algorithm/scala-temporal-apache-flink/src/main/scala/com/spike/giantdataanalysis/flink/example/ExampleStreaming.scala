package com.spike.giantdataanalysis.flink.example

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.common.JobExecutionResult
import com.spike.giantdataanalysis.flink.example.models.WordWithCount

/**
 * {@link DataStream} API示例.
 */
object ExampleStreaming {
  def main(args : Array[String]) : Unit = {
    val port : Int = 9000

    val env : StreamExecutionEnvironment =
      //prodStreamEnv
      localStreamEnv

    val text =
      //env.socketTextStream("localhost", port, '\n')
      env.fromElements("what is real is rational", "and what is rational is real")

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text.flatMap { w ⇒ w.split("\\s") }
      .map { w ⇒ WordWithCount(w, 1) }
      .keyBy("word")
      //.timeWindow(Time.seconds(5), Time.seconds(1))
      .sum("count")

    // WARN: DEBUG ONLY
    // print the results with a single thread, rather than in parallel
    //windowCounts.print().setParallelism(1)

    // launch the execution
    val result : JobExecutionResult = env.execute("Socket Window WordCount")
    println(s"JobID=${result.getJobID}")

    // mock sink
    import scala.collection.JavaConverters._
    import org.apache.flink.contrib.streaming.DataStreamUtils
    val mockSink = DataStreamUtils.collect(windowCounts.javaStream).asScala
    println(s"Result=${mockSink.toList}")
  }
}