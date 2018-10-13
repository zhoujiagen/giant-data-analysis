package com.spike.giantdataanalysis.flink.example.streaming


/**
  * 1 Time Characteristic: event time, processing time
  * 2 timestamp and watermark
  */
package object time {
  // stream
  import org.apache.flink.streaming.api.scala._

  // DataStream API extension
  // import org.apache.flink.streaming.api.scala.extensions._


  val localStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1)
  val prodStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  // 设置时间特征
  import org.apache.flink.streaming.api.TimeCharacteristic
  //  localStreamEnv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
  localStreamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
  //  localStreamEnv.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)
}
