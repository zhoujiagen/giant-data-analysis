package com.spike.giantdataanalysis.flink.example

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import models._

/**
 * Operator示例:
 * (1) Transformation
 * (2) physical partitioning
 * (3) task chaining and resource group
 */
object ExampleStreamingOperators {

  def main(args : Array[String]) : Unit = {

    val env = localStreamEnv

    transformation(env)

    env.execute("ExampleStreamingOperators")
  }

  def transformation(env : StreamExecutionEnvironment) : Unit = {
    //    val pointDS : DataStream[Point] = env.fromCollection(generatePoints())
    //    pointDS.mapWith {
    //      case Point(x, y) ⇒ Point(x + 1, y + 1)
    //    }.print()

    val uaDS : DataStream[UserActivity] = env.fromCollection(generateUserActivity(10))
    uaDS.keyBy("username")
      .reduce { (ua1 : UserActivity, ua2 : UserActivity) ⇒
        ua1.copy(lastMs = ua1.lastMs + ua2.lastMs)
      }.print()

  }

}