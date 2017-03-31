package com.spike.giantdataanalysis.spark.example.rdd

import com.spike.giantdataanalysis.spark.support.Datasets
import org.apache.spark.storage.StorageLevel

/**
 * 持久化/缓存
 */
object RDDCaching {
  def main(args: Array[String]): Unit = {
    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    val input = sc.textFile(inputFile)

    input.persist(StorageLevel.MEMORY_ONLY)
    println(input.count())
    println(input.collect().mkString(", "))

    // 阻塞移除
    input.unpersist(true)
  }
}