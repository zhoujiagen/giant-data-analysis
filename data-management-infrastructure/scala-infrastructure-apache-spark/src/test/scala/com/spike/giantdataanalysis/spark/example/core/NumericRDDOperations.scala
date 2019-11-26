package com.spike.giantdataanalysis.spark.example.core

/**
 * 数值RDD操作
 */
object NumericRDDOperations {
  def main(args : Array[String]) : Unit = {
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    println(rdd.count())
    println(rdd.mean())
    println(rdd.sum())
    println(rdd.max())
    println(rdd.min())
    println(rdd.variance())
    println(rdd.stdev())
    // 采样
    println(rdd.sampleVariance())
    println(rdd.sampleStdev())

    val statsCounter = rdd.stats()
    println(statsCounter.toString())
    println(statsCounter.mean)

  }
}