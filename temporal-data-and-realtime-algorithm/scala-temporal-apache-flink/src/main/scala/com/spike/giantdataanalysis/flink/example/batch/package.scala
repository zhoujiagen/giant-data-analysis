package com.spike.giantdataanalysis.flink.example

package object batch {
  // batch
  import org.apache.flink.api.scala._

  // DataSet API extension
  //  import org.apache.flink.api.scala.extensions._

  val localBatchEnv: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(1)
  val prodBatchEnv: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
}
