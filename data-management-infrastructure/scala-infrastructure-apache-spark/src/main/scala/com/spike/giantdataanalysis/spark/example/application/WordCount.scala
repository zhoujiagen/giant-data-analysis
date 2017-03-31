package com.spike.giantdataanalysis.spark.example.application

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import com.spike.giantdataanalysis.spark.support.Datasets

/**
 * 文本中单词计数
 * @author zhoujiagen
 */
object WordCount {
  def main(args : Array[String]) {
    // configuration and context
    val conf = new SparkConf().setMaster("local").setAppName("Word Count")
    val sc = new SparkContext(conf)

    val inputFile = Datasets.DATA_DIR + "spark-README.md"
    println(inputFile)
    val input = sc.textFile(inputFile)
    val words = input flatMap { line ⇒ line.split(" ") }
    val counts = words map { word ⇒ (word, 1) } reduceByKey { case (x, y) ⇒ x + y }

    // save
    counts.saveAsTextFile(Datasets.OUTPUT_DATA_DIR)
  }
}