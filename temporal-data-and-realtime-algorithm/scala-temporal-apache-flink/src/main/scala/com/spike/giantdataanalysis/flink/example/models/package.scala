
package com.spike.giantdataanalysis.flink.example

import java.util.Date

package object models {
  // Data type for words with count
  case class WordWithCount(word : String, count : Long)

  case class Point(x : Double, y : Double)

  case class TimeSeriesPoint(metric : String, timestamp : Long, value : Double, tags : Map[String, String])

  def generateTimeSeriesPoints(startTime : Long = new Date().getTime) : List[TimeSeriesPoint] = {

    List(TimeSeriesPoint("sys.if.bytes.out", new Date().getTime, 1,
      Map[String, String]("host" -> "web01", "colo" -> "lga", "interface" -> "eth0")))
  }

}