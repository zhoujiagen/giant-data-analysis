package com.spike.giantdataanalysis.spark.example

/**
 * 累加器				- Accumulators
 * 广播变量			- BroadcastVariables
 * 分区级别(per partition)初始化
 * 数值RDD操作	- NumericRDDOperations
 */
package object core {
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  import org.apache.spark.SparkContext._

  // configuration and context
  val conf = new SparkConf().setMaster("local").setAppName("RDD Operations")
  val sc = new SparkContext(conf)
}