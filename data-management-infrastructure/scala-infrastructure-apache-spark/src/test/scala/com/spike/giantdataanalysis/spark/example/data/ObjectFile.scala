package com.spike.giantdataanalysis.spark.example.data

import com.spike.giantdataanalysis.spark.support.Datasets
import org.apache.spark.rdd.RDD

/**
 * 对象文件使用Java序列化
 * 一般用于Spark Job之间的通信
 */
object ObjectFile {
  def main(args : Array[String]) : Unit = {
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    val path = Datasets.OUTPUT_DATA_DIR + "objectFile"
    rdd.saveAsObjectFile(path)

    val read : RDD[Int] = sc.objectFile(path)
    read.foreach(println)
  }
}