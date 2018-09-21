package com.spike.giantdataanalysis.flink.example.streaming.operator

import org.apache.flink.api.common.functions.AggregateFunction

class SimpleAverageAggregate extends AggregateFunction[(String, Int), (Int, Int), Int] {
  override def createAccumulator(): (Int, Int) = (0, 0)

  override def add(value: (String, Int), accumulator: (Int, Int)): (Int, Int) =
    (accumulator._1 + value._2, accumulator._2 + 1)

  // accumulator: (和, 数量)
  override def getResult(accumulator: (Int, Int)): Int =
    accumulator._1 / accumulator._2

  override def merge(a: (Int, Int), b: (Int, Int)): (Int, Int) =
    (a._1 + b._1, a._2 + b._2)
}