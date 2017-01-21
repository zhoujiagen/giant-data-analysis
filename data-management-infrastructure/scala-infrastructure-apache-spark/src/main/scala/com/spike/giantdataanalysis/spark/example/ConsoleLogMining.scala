package com.spike.giantdataanalysis.spark.example

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * 文本中日志挖掘
 * @author zhoujiagen
 */
object ConsoleLogMining {
  def main(args: Array[String]) {
    // data
    val logFile = DATA_DIR + "log.out"

    // configuration and context
    val conf = new SparkConf().setAppName("Console Log Mining")
    val sc = new SparkContext(conf)

    // application operations
    val lines = sc.textFile(logFile, 2).cache()
    val errors = lines.filter(_.contains("ERROR"))
    errors.persist()

    // application output
    println("errors' count is: " + errors.count())

    // errors about mysql
    val errosAboutMySQL = errors.filter { _.contains("MYSQL") }
    println("errors about mysql count is: " + errosAboutMySQL.count())

    // errors about HDFS, retrieval the event time
    val errorTimesAboutHDFS = errors.filter { _.contains("HDFS") }.map { _.split("\t")(3) }.collect()
    for (errorTime ← errorTimesAboutHDFS)
      println(errorTime)

  }
}
