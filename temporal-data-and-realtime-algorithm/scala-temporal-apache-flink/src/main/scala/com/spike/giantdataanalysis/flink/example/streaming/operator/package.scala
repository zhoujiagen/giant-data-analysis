package com.spike.giantdataanalysis.flink.example.streaming

package object operator {

  import org.apache.flink.streaming.api.scala._

  def stream(env: StreamExecutionEnvironment): DataStream[(String, Int)] = {
    //    env.socketTextStream("localhost", 9999)
    //      .flatMap {
    //        _.toLowerCase.split("\\W+").filter {
    //          _.nonEmpty
    //        }
    //      }.map {
    //      (_, 1)
    //    }
    import scala.util.Random
    val rnd = new Random()
    env.fromCollection(List.fill(100)((rnd.nextString(1), rnd.nextInt())))
  }
}
