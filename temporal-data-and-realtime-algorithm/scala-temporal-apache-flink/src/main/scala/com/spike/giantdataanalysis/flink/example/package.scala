package com.spike.giantdataanalysis.flink

package object example {

  // batch
  import org.apache.flink.api.scala._
  // stream
  import org.apache.flink.streaming.api.scala._

  // DataSet API extension
  //  import org.apache.flink.api.scala.extensions._
  // DataStream API extension
  //  import org.apache.flink.streaming.api.scala.extensions._

  val localStreamEnv : StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1)
  val prodStreamEnv : StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val localBatchEnv : ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(1)
  val prodBatchEnv : ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
}