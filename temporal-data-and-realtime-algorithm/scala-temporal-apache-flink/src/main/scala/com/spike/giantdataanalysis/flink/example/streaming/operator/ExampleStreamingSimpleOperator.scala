package com.spike.giantdataanalysis.flink.example.streaming.operator

import java.util.Date

import com.spike.giantdataanalysis.flink.example.Point
import com.spike.giantdataanalysis.flink.example.model.{Randoms, UserNames}
import com.spike.giantdataanalysis.flink.example.streaming._
import org.apache.flink.streaming.api.scala._


/**
  * Operator示例.
  */
object ExampleStreamingSimpleOperator {

  def main(args: Array[String]): Unit = {

    val env = localStreamEnv

    pointTransformation(env)
    userActivityTransform(env)

    env.execute()
  }

  def pointTransformation(env: StreamExecutionEnvironment): Unit = {
    val pointDS: DataStream[Point] = env.fromCollection(generatePoints())

    // Scala API扩展
    import org.apache.flink.streaming.api.scala.extensions._
    pointDS.mapWith {
      case Point(x, y) ⇒ Point(x + 1, y + 1)
    }.print()
  }

  def userActivityTransform(env: StreamExecutionEnvironment): Unit = {
    val uaDS: DataStream[UserActivity] = env.fromCollection(generateUserActivity(10))
    uaDS.keyBy("username")
      .reduce { (ua1: UserActivity, ua2: UserActivity) ⇒
        ua1.copy(lastMs = ua1.lastMs + ua2.lastMs)
      }.print()
  }


  def generatePoints(count: Int = 10): List[Point] = {
    val list = collection.mutable.ListBuffer[Point]()

    for (i ← 1 to count) {
      list.append(Point(i, i + 1))
    }

    list.toList
  }


  def generateTimeSeriesPoints(startTime: Long = new Date().getTime): List[TimeSeriesPoint] = {
    List(TimeSeriesPoint("sys.if.bytes.out", new Date().getTime, 1,
      Map[String, String]("host" -> "web01", "colo" -> "lga", "interface" -> "eth0")))
  }


  var previousRequest: Request = Create(Randoms.ID(), StringResource(""))

  def generateUserActivity(count: Int = 10): List[UserActivity] = {
    val list = collection.mutable.ListBuffer[UserActivity]()

    def nextRequest(): Request = {
      previousRequest = previousRequest match {
        case Create(_, _) ⇒ Read(Randoms.ID())
        case Read(_) ⇒ Update(Randoms.ID(), StringResource(""))
        case Update(_, _) ⇒ Delete(Randoms.ID())
        case Delete(_) ⇒ Create(Randoms.ID(), StringResource(""))
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
}