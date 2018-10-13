package com.spike.giantdataanalysis.flink.example.streaming

/**
  * 1 different kind of state in Flink applicaiton
  * 2 checkpointing
  * 3 queryable state
  * 4 serialization in managed state
  */
package object state {

  // stream
  import org.apache.flink.streaming.api.scala._

  // DataStream API extension
  import org.apache.flink.streaming.api.scala.extensions._

  val localStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1)
  val prodStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


}
