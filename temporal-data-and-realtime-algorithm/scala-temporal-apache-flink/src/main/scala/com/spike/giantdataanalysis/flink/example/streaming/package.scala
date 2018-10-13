package com.spike.giantdataanalysis.flink.example

package object streaming {
  // stream
  import org.apache.flink.streaming.api.scala._

  // DataStream API extension
  import org.apache.flink.streaming.api.scala.extensions._


  val localStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1)
  val prodStreamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  //---------------------------------------------------------------------------
  // Domain
  //---------------------------------------------------------------------------

  trait Resource

  sealed trait Request {
    val key: String
  }

  case class Create(key: String, value: Resource) extends Request

  case class Read(key: String) extends Request

  case class Update(key: String, value: Resource) extends Request

  case class Delete(key: String) extends Request

  case class Response[+T](success: Boolean, value: T)

  // user activity
  case class UserActivity(username: String,
                          timestamp: Long,
                          request: Request,
                          response: Response[Any],
                          lastMs: Long)

  case class StringResource(value: String) extends Resource

  // time series data point
  case class TimeSeriesPoint(metric: String,
                             timestamp: Long,
                             value: Double,
                             tags: Map[String, String])

}
