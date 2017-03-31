package com.spike.giantdataanalysis.spark.example.rdd

import com.spike.giantdataanalysis.spark.support.Datasets

object RDDCreation {
  def main(args: Array[String]): Unit = {
    // 创建RDD的两种方式: 从外部数据源加载; 在驱动程序中并行化集合
    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    val input = sc.textFile(inputFile)
    println(input.toDebugString)

    val lines = sc.parallelize(List("pandas", "i like pandas"))
    println(lines.toDebugString)
  }
}