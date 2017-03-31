package com.spike.giantdataanalysis.spark.example

/**
 *  <pre>
 *  加载和保存数据
 *  
 *  1 文件格式
 *  2 文件系统
 *  3 Spark SQL的结构化数据
 *  4 数据库: JDBC, HBase, ES
 *  
 *  </pre>
 */
package object data {
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  import org.apache.spark.SparkContext._

  // configuration and context
  val conf = new SparkConf().setMaster("local").setAppName("RDD Operations")
  val sc = new SparkContext(conf)
}