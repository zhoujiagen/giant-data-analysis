package com.spike.giantdataanalysis.flink.example.batch

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem

/**
  * 在Cluster上执行.
  */
object ExampleBatchClusterExecution {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.createRemoteEnvironment(
      host = "localhost",
      port = 8081, // rest port
      "target/scala-2.11/scala-temporal-apache-flink_2.11-1.0.0.jar") // local path
    env.fromElements(1L, 2L, 3L, 4L, 5L)
      .filter(_ <= 3L)
      .writeAsText("/tmp/output.txt", FileSystem.WriteMode.OVERWRITE) // path in cluster
    env.execute()
  }
}
