package com.spike.giantdataanalysis.hudi

import org.apache.spark.sql.SparkSession

package object example {

  import org.apache.spark.{SparkConf, SparkContext}

  // configuration and context
  val conf = new SparkConf().setMaster("local").setAppName("Hudi App")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  // Main entry point for Spark functionality. A SparkContext represents the connection to a Spark
  // cluster, and can be used to create RDDs, accumulators and broadcast variables on that cluster.
  val sc = new SparkContext(conf)
  // The entry point to programming Spark with the Dataset and DataFrame API.
  val ss = SparkSession.builder() //
    .config(conf)
    .getOrCreate()
}
