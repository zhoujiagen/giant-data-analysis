package com.spike.giantdataanalysis.spark.example.rdd

import org.apache.spark.rdd.RDD

object RDDWithFunctions {
  class Search(val query: String) {
    def isMatch(sentence: String): Boolean = {
      sentence.contains(query)
    }

    def getMethodReference(rdd: RDD[String]): RDD[String] = {
      //引用this.isMatch, 将传递this
      rdd filter isMatch
    }

    def getFieldReference(rdd: RDD[String]): RDD[String] = {
      // 引用this.query, 将传递this
      rdd flatMap { s ⇒ s.split(query) }
    }

    def getNoneReference(rdd: RDD[String]): RDD[String] = {
      // 将字段定义为局部变量
      val _query = this.query
      rdd flatMap { s ⇒ s.split(_query) }
    }

  }
}