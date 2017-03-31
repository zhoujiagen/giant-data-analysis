package com.spike.giantdataanalysis.spark.example.rdd

import com.spike.giantdataanalysis.spark.support.Datasets
import org.apache.spark.rdd.RDD

/**
 * <pre>
 * Key/Value pair RDD Actions
 * 
 * countByKey()
 * collectAsMap()
 * lookup()
 * </pre>
 */
object PairRDDActions {
  def main(args : Array[String]) : Unit = {
    //    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    //    val input = sc.textFile(inputFile)
    //
    //    val input20 = input.take(20)
    //    // (行第一个单词, 行)
    //    val pairs = input20 map { x ⇒ (x.split(" ")(0), x) }
    //    val pairRDD: RDD[(String, String)] = sc.parallelize(pairs)
    //    pairRDD foreach println

    val pairRDD = sc.parallelize(List((1, 2), (3, 4), (3, 6)))
    // countByKey: (1,1), (3,2)
    pairRDD.countByKey().foreach(println)
    // collectAsMap: (1,2), (3,6)
    pairRDD.collectAsMap().foreach(println)
    // lookup: 4, 6
    pairRDD.lookup(3).foreach(println)
    
  }
}