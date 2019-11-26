package com.spike.giantdataanalysis.spark.example.rdd

import org.apache.spark.HashPartitioner

/**
 * <pre>
 * 1 分区带来提升的操作
 * cogroup()
 * groupWith()
 * join(), leftOuterJoin(), rightOuterJoin()
 * groupByKey()
 * reduceByKey()
 * combineByKey()
 * lookup()
 *
 * 2 影响分区的操作
 * cogroup()
 * groupWith()
 * join(), leftOuterJoin(), rightOuterJoin()
 * groupByKey()
 * reduceByKey()
 * combineByKey()
 * partitionBy()
 * sort()
 * mapValues()
 * flatMapValues()
 * filter()
 *
 * </pre>
 */
object RDDWithParitions2 {
  def main(args : Array[String]) : Unit = {
    val pairs = sc.parallelize(List((1, 1), (2, 2), (3, 3)))
    // 分区器
    println(pairs.partitioner)

    // 指定分区器
    val partioned = pairs.partitionBy(new HashPartitioner(2))
    println(partioned.partitioner)
  }
}