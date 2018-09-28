package com.spike.giantdataanalysis.flink.example

package object batch {

  import org.apache.flink.api.scala._ // batch
  import org.apache.flink.api.scala.extensions._ // DataSet API extension

  val localBatchEnv: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(1)
  val prodBatchEnv: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
}
