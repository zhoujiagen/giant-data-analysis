package com.spike.giantdataanalysis.spark.example.core

/**
 * 广播变量: 只读值
 */
object BroadcastVariables {
  def main(args : Array[String]) : Unit = {
    val readOnlyLargeMap = Map("CN" -> 1, "US" -> 2, "UN" -> 3)
    val bv = sc.broadcast(readOnlyLargeMap)

    sc.parallelize(List("CN", "UN")).map {
      key ⇒ (key, bv.value.get(key))
    }.foreach(println)
  }
}