package com.spike.giantdataanalysis.flink.example

import org.apache.flink.api.scala._
import org.apache.flink.api.scala.extensions._
import org.apache.flink.api.java.io.LocalCollectionOutputFormat
import org.apache.flink.api.common.JobExecutionResult

/**
 * Validation of Scala API Extensions.
 */
object ExampleScalaAPIExtensions {
  def main(args : Array[String]) : Unit = {
    extension_acceptPartialFunctions()
  }

  // extension: accept partial function
  def extension_acceptPartialFunctions() {
    val data : DataSet[(Int, String, Double)] =
      localBatchEnv.fromCollection(List((1, "hello", 1.0), (2, "flink", 2.0)))

    import collection.mutable._
    import collection.JavaConverters._
    val mockSinkList = ArrayBuffer[String]().asJava

    data.mapWith {
      case (id, name, value) ⇒ s"${id},${name}, ${value}"
    }

      // trigger a RuntimeException: No new data sinks have been defined since the last execution.
      //.print()
      .output(new LocalCollectionOutputFormat(mockSinkList))

    val result : JobExecutionResult = localBatchEnv.execute("ExampleValidateScalaAPIExtensions")
    println(result.getJobID) // JobID
    println(mockSinkList)
  }

}