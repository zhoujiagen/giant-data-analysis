package com.spike.giantdataanalysis.flink.example.testing

import org.apache.flink.api.common.functions.ReduceFunction
import org.scalatest.{FlatSpec, Matchers}


class SumReduceFunction extends ReduceFunction[Long] {
  override def reduce(value1: Long, value2: Long): Long =
    value1 + value2
}

// 单元测试
class TestSumReduceFunction extends FlatSpec with Matchers {
  "SumReduceFunction" should "add values" in {
    val sumReduce = new SumReduceFunction
    sumReduce.reduce(1L, 2L) should be(3L)
  }
}