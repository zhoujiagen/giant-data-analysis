
package com.spike.giantdataanalysis.flink.example

import java.util.Date
import com.spike.giantdataanalysis.flink.example.model.Randoms
import com.spike.giantdataanalysis.flink.example.model.UserNames

package object models {
  // Data type for words with count
  case class WordWithCount(word : String, count : Long)

  // 2D point
  case class Point(
    x : Double,
    y : Double)

  def generatePoints(count : Int = 10) : List[Point] = {
    val list = collection.mutable.ListBuffer[Point]()

    for (i ← 1 to count) {
      list.append(Point(i, i + 1))
    }

    list.toList
  }

  // time series data point
  case class TimeSeriesPoint(
    metric :    String,
    timestamp : Long,
    value :     Double,
    tags :      Map[String, String])

  def generateTimeSeriesPoints(startTime : Long = new Date().getTime) : List[TimeSeriesPoint] = {
    List(TimeSeriesPoint("sys.if.bytes.out", new Date().getTime, 1,
      Map[String, String]("host" -> "web01", "colo" -> "lga", "interface" -> "eth0")))
  }

  trait Resource
  sealed trait Request {
    val key : String
  }
  case class Create(key : String, value : Resource) extends Request
  case class Read(key : String) extends Request
  case class Update(key : String, value : Resource) extends Request
  case class Delete(key : String) extends Request

  case class Response[+T](success : Boolean, value : T)

  // user activity
  case class UserActivity(
    username :  String,
    timestamp : Long,
    request :   Request,
    response :  Response[Any],
    lastMs :    Long)

  case class StringResource(value : String) extends Resource
  var previousRequest : Request = Create(Randoms.ID(), StringResource(""))
  def generateUserActivity(count : Int = 10) : List[UserActivity] = {
    val list = collection.mutable.ListBuffer[UserActivity]()

      def nextRequest() : Request = {
        previousRequest = previousRequest match {
          case Create(_, _) ⇒ Read(Randoms.ID())
          case Read(_)      ⇒ Update(Randoms.ID(), StringResource(""))
          case Update(_, _) ⇒ Delete(Randoms.ID())
          case Delete(_)    ⇒ Create(Randoms.ID(), StringResource(""))
        }
        previousRequest
      }

    for (i ← 1 to count) {
      list.append(
        UserActivity(
          UserNames.nextNameSmall(),
          new Date().getTime,
          nextRequest(),
          Response[String](Randoms.BOOL(), "ok"),
          100))
    }
    list.toList
  }

  def main(args : Array[String]) : Unit = {
    generateUserActivity().foreach(println)
  }

}