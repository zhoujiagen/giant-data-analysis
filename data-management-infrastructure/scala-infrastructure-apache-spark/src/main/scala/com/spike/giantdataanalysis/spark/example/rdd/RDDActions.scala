package com.spike.giantdataanalysis.spark.example.rdd

import com.spike.giantdataanalysis.spark.support.Datasets
import java.util.Date

/**
 * RDD Actions
 *
 * collect()
 * count()
 * countByValue()
 * take(num)
 * top(num)
 * takeOrdered(num)(ordering)
 * takeSample(withReplacement, num, [seed])
 * reduce(func)
 * aggregate(zeroValue)(seqOp, combOp)
 * foreach(func)
 */
object RDDActions {
  def main(args: Array[String]): Unit = {
    //    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    //    val input = sc.textFile(inputFile)
    //    val worldRdd = input filter { line ⇒ line.contains("world") }
    //    val cnt = worldRdd.count()
    //    println(cnt)

    val rdd = sc.parallelize(List(1, 2, 3, 3))
    println(rdd.collect().mkString(", "))
    println(rdd.count())
    println(rdd.countByValue()(new Ordering[Int]() {
      def compare(x: Int, y: Int): Int = {
        y - x
      }
    }).mkString(", "))
    println(rdd.take(2).mkString(", "))
    println(rdd.top(2).mkString(", "))
    println(rdd.takeOrdered(2)(new Ordering[Int]() {
      def compare(x: Int, y: Int): Int = {
        y - x
      }
    }).mkString(", "))
    println(rdd.takeSample(true, 2, new Date().getTime).mkString(", "))
    println(rdd.reduce((x, y) ⇒ x + y))

    //(zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U): U
    val zeroValue = (0, 0)
    val seqOp = (acc: (Int, Int), value: Int) ⇒ (acc._1 + value, acc._2 + 1)
    val combOp = (acc1: (Int, Int), acc2: (Int, Int)) ⇒ (acc1._1 + acc2._1, acc1._2 + acc2._2)
    println(rdd.aggregate(zeroValue)(seqOp, combOp))

    rdd foreach println
  }
}