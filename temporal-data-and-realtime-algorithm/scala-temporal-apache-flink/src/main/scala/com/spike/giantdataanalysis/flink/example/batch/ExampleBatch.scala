package com.spike.giantdataanalysis.flink.example.batch


import org.apache.flink.api.scala._

object ExampleBatch {
  def main(args: Array[String]): Unit = {
    val env = localBatchEnv
    val text = env.fromElements(
      "Who's there?",
      "I think I hear them. Stand, ho! Who's there?")

    val counts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.nonEmpty
      }
    }
      .map {
        (_, 1)
      }
      .groupBy(0)
      .sum(1)

    counts.print()

    env.execute()
  }
}
