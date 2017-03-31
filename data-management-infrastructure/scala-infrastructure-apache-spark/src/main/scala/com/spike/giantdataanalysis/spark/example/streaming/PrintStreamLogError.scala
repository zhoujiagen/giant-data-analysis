package com.spike.giantdataanalysis.spark.example.streaming

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel

/**
 * 输出日志流中错误
 * @author zhoujiagen
 * @see com.spike.giantdataanalysis.spark.example.support.BlockingTCPServer
 */
object PrintStreamLogError {
  def main(args: Array[String]): Unit = {
    // configuration and context
    val conf = new SparkConf().setAppName("Print Stream Log Error")
    //		val sc = new SparkContext(conf)

    // create StreamingContext
    val ssc = new StreamingContext(conf, Seconds(1))

    // create a DStream
    val lines = ssc.socketTextStream("localhost", 7777, StorageLevel.MEMORY_ONLY)

    // filter error lines
    val errorLines = lines.filter { _.contains("error") }
    // output
    errorLines.print()

    // start
    ssc.start()

    // wait for the job to finish
    ssc.awaitTermination()
  }
}