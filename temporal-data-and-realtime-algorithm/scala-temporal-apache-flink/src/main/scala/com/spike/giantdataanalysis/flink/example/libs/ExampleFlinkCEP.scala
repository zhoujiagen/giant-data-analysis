package com.spike.giantdataanalysis.flink.example.libs

import java.util
import java.util.Date

import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.IterativeCondition
import org.apache.flink.cep.{CEP, PatternSelectFunction}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class DummyCEPEvent(val id: Long, val name: String, val timestamp: Long)

case class DummyCEPPatternMatchResult(start: DummyCEPEvent, middle: DummyCEPEvent, end: DummyCEPEvent)

/**
  * Flink CEP示例.
  */
object ExampleFlinkCEP {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val inputStream = env.fromCollection(genData(10000)).assignAscendingTimestamps(_.timestamp)

    val pattern: Pattern[DummyCEPEvent, DummyCEPEvent] =
      Pattern.begin[DummyCEPEvent]("start", AfterMatchSkipStrategy.skipPastLastEvent()) // skip all partitial matching
        // next
        .next("middle").where(new IterativeCondition[DummyCEPEvent] {
        override def filter(value: DummyCEPEvent, ctx: IterativeCondition.Context[DummyCEPEvent]): Boolean = value.name == "error"
      })
        // followBy
        .followedBy("end").where(new IterativeCondition[DummyCEPEvent] {
        override def filter(value: DummyCEPEvent, ctx: IterativeCondition.Context[DummyCEPEvent]): Boolean = value.name == "critical"
      })
        // within
        .within(Time.seconds(1))

    val patternStream = CEP.pattern[DummyCEPEvent](inputStream.javaStream, pattern)
    patternStream.select(new PatternSelectFunction[DummyCEPEvent, DummyCEPPatternMatchResult] {
      override def select(pattern: util.Map[String, util.List[DummyCEPEvent]]): DummyCEPPatternMatchResult = {
        val start = pattern.get("start").get(0)
        val middle = pattern.get("middle").get(0)
        val end = pattern.get("end").get(0)
        DummyCEPPatternMatchResult(start, middle, end)
      }
    }).print()

    env.execute()
  }


  def genData(n: Int): Seq[DummyCEPEvent] = {


    def newCEPEvent(id: Long, rnd: Random): DummyCEPEvent = DummyCEPEvent(id,
      if (rnd.nextBoolean()) "error" else "critical", new Date().getTime
    )

    val rnd = new Random()
    val result = ListBuffer[DummyCEPEvent]()
    for (i <- 1 to n) {
      result.append(
        newCEPEvent(i, rnd)
      )
      //Thread.sleep(100L)
    }

    result.toList
  }
}
