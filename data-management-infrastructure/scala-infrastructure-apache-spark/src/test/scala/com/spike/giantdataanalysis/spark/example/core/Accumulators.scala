package com.spike.giantdataanalysis.spark.example.core

import com.spike.giantdataanalysis.spark.support.Datasets

/**
 * 累加器
 */
object Accumulators {
  def main(args : Array[String]) : Unit = {
    val input = sc.textFile(Datasets.DATA_DIR + "spark-README.md")

    val blankLineAccumulator = sc.accumulator(0)
    val words = input.flatMap(line ⇒ {
      if (line == "") {
        blankLineAccumulator += 1
      }
      line.split(" ")
    }).collect()
    println(s"Count of blank lines: ${blankLineAccumulator.value}")
    println(words.mkString(" "))
    
  }
}