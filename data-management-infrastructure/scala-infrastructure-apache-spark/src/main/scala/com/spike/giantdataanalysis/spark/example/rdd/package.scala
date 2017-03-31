package com.spike.giantdataanalysis.spark.example

/**
 * 1 RDD操作: transformation, action
 * transformation是惰性求值的, Spark在见到action前不会开始执行.
 *
 * 2 Key/Value pair RDD
 * 3 数据分区(Data Partition)
 * 4 RDD持久化/缓存
 */
package object rdd {
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  import org.apache.spark.SparkContext._

  import org.apache.hadoop.io.IntWritable
  import org.apache.hadoop.io.Text

  // configuration and context
  val conf = new SparkConf().setMaster("local").setAppName("RDD Operations")
  // 注册序列化类
  // REF: http://stackoverflow.com/questions/29876681/hadoop-writables-notserializableexception-with-apache-spark-api
  conf.registerKryoClasses(Array(classOf[IntWritable], classOf[Text]))
  val sc = new SparkContext(conf)

  // 自定义分区器: URL的域名一致分到一个分区中
  import org.apache.spark.Partitioner
  class DomainNamePartitioner(numPairs : Int) extends Partitioner {
    override def numPartitions : Int = numPairs

    override def getPartition(key : Any) : Int = {
      val domain = new java.net.URL(key.toString()).getHost()
      val code = (domain.hashCode() % numPartitions)
      if (code < 0) {
        code + numPartitions
      }
      else {
        code
      }
    }

    override def equals(other : Any) : Boolean = other match {
      case dnp : DomainNamePartitioner ⇒ dnp.numPartitions == numPartitions
      case _                           ⇒ false
    }
  }
}