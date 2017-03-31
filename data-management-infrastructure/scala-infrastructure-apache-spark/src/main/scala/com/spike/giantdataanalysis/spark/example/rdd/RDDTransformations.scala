package com.spike.giantdataanalysis.spark.example.rdd

import com.spike.giantdataanalysis.spark.support.Datasets

/**
 * RDD Transformations
 *
 * filter()
 * map()
 * flatMap()
 * distinct()
 * union()
 * intersection()
 * subtract()
 * cartesian()
 */
object RDDTransformations {
  def main(args: Array[String]): Unit = {
    //    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    //    val input = sc.textFile(inputFile)
    //    val worldRDD = input filter { line ⇒ line.contains("world") }
    //    println(worldRDD.toDebugString)
    //    val oldRDD = input filter { line ⇒ line.contains("old") }
    //    println(oldRDD.toDebugString)
    //    val oldWorldRDD = worldRDD.union(oldRDD)
    //    println(oldWorldRDD.toDebugString)

    // 元素级别transformation
    val input = sc.parallelize(List(1, 2, 3, 4))
    val result = input map { x ⇒ x * x }
    println(result.collect().mkString(", "))

    val lines = sc.parallelize(List("Spark rocks", "is that true?"))
    val words = lines flatMap { line ⇒ line.split(" ") }
    words foreach println

    // 类集合操作
    val rdd1 = sc.parallelize(List("coffee", "coffee", "panda", "monkey", "tea"))
    val rdd2 = sc.parallelize(List("coffee", "monkey", "kitty"))
    // distinct(): 去除冗余, with shuffle
    // monkey, coffee, panda, tea
    println(rdd1.distinct().collect().mkString(", "))
    // union(): 不去除冗余
    // coffee, coffee, panda, monkey, tea, coffee, monkey, kitty
    println(rdd1.union(rdd2).collect().mkString(", "))
    // intersection(): 去除冗余, with shuffle
    // monkey, coffee
    println(rdd1.intersection(rdd2).collect().mkString(", "))
    // subtract(): with shuffle
    // tea, panda
    println(rdd1.subtract(rdd2).collect().mkString(", "))
    // cartesian
    case class User(no: Int)
    case class Venue(name: String)
    val userRDD = sc.parallelize(List(User(1), User(2), User(3)))
    val venueRDD = sc.parallelize(List(Venue("Betabrand"), Venue("Asha Tea House"), Venue("Ritual")))
    userRDD.cartesian(venueRDD) foreach { x ⇒ println(x._1, x._2) }
  }
}