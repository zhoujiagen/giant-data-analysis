package com.spike.giantdataanalysis.flink.example.batch

import com.spike.giantdataanalysis.flink.example.Point
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.extensions._

/**
  * [[DataSet]] API实例.
  */
object ExampleBatchWithApiExtension {
  def main(args: Array[String]): Unit = {
    val benv = localBatchEnv

    val dataSet: DataSet[Point] = benv.fromElements(
      Point(1, 3),
      Point(2, 4),
      Point(3, 5))

    val gds: GroupedDataSet[(String, Double)] = dataSet.filterWith {
      case Point(x, y) ⇒ x > 1
    }.reduceWith {
      case (Point(x1, y1), Point(x2, y2)) ⇒ Point(x1 + x2, y1 + y2)
    }.mapWith {
      case Point(x, y) ⇒ (x, y)
    }.flatMapWith {
      case (x, y) ⇒ Seq("x" -> x, "y" -> y)
    }.groupingBy {
      case (x, y) ⇒ x
    }

    gds.first(2).print()

    val jobExecutionResult = benv.execute()
    println(jobExecutionResult.getJobID)
  }
}