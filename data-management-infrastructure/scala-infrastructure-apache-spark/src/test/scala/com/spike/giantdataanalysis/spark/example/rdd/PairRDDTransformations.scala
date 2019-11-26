package com.spike.giantdataanalysis.spark.example.rdd

/**
 * <pre>
 * Key/Value pair RDD transformation
 *
 * == pair RDDs are still RDDS.
 *
 * == on one pair RDD:
 * reduceByKey(func)
 * groupByKey()
 * combineByKey(createCombiner, mergeValue, mergeCombiner, partitioner)
 * mapValues(func)
 * flatMapValues(func)
 * keys()
 * values()
 * sortByKey()
 *
 * == on two pair RDDs:
 * subtractByKey()
 * join()
 * rightOuterJoin()
 * leftOuterJoin()
 * cogroup()
 * </pre>
 */
object PairRDDTransformations {
  def main(args: Array[String]): Unit = {
    val pairRDD = sc.parallelize(List((1, 2), (3, 4), (3, 6)))

    // reduceByKey: (1,2), (3,10)
    pairRDD.reduceByKey((x, y) ⇒ x + y).foreach(println)
    // groupByKey: (1,CompactBuffer(2)), (3,CompactBuffer(4, 6))
    pairRDD.groupByKey().foreach(println)
    // combineByKey: (1,(2,1)), (3,(10,2))
    // createCombiner: Int => C, mergeValue: (C, Int) => C, mergeCombiners: (C, C) => C
    val createCombiner = (v: Int) ⇒ (v, 1)
    val mergeValue = (acc: (Int, Int), v: Int) ⇒ (acc._1 + v, acc._2 + 1)
    val mergeCombiners = (acc1: (Int, Int), acc2: (Int, Int)) ⇒ (acc1._1 + acc2._1, acc1._2 + acc2._2)
    pairRDD.combineByKey(createCombiner, mergeValue, mergeCombiners).collectAsMap().foreach(println)
    // mapValues: (1,3), (3,5), (3,7)
    pairRDD.mapValues(x ⇒ x + 1).foreach(println)
    // flatMapValues: (1,2), (1,3), (1,4), (1,5), (3,4), (3,5)
    pairRDD.flatMapValues(x ⇒ (x to 5)).foreach(println)
    // keys: 1, 3, 3
    pairRDD.keys.foreach(println)
    // values: 2, 4, 6
    pairRDD.values.foreach(println)
    // sortByKey: (3,4), (3,6), (1,2)
    pairRDD.sortByKey(ascending = false).foreach(println)

    // 两个RDD上操作
    val otherRDD = sc.parallelize(List((3, 9)))
    // subtractByKey: (1,2)
    pairRDD.subtractByKey(otherRDD).foreach(println)
    // join(自然连接): (3,(4,9)), (3,(6,9))
    pairRDD.join(otherRDD).foreach(println)
    // rightOuterJoin: (3,(Some(4),9)), (3,(Some(6),9))
    pairRDD.rightOuterJoin(otherRDD).foreach(println)
    // leftOuterJoin: (1,(2,None)), (3,(4,Some(9))), (3,(6,Some(9)))
    pairRDD.leftOuterJoin(otherRDD).foreach(println)
    // cogroup: (1,(CompactBuffer(2),CompactBuffer())), (3,(CompactBuffer(4, 6),CompactBuffer(9)))
    pairRDD.cogroup(otherRDD).foreach(println)
  }
}