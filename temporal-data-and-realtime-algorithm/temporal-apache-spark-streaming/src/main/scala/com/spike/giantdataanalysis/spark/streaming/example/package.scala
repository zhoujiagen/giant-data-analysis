

package com.spike.giantdataanalysis.spark.streaming

package object example {

  import org.apache.spark.SparkConf
  import org.apache.spark.streaming._
  
  // configuration and context
  val conf = new SparkConf().setMaster("local[2]").setAppName("Spark Streaming Examples")
  val ssc = new StreamingContext(conf, Seconds(1))
}