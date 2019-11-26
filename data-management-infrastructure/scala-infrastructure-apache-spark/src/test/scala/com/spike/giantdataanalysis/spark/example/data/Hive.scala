package com.spike.giantdataanalysis.spark.example.data

import com.spike.giantdataanalysis.spark.support.Datasets

/**
 * + spark hive
 * http://spark.apache.org/docs/1.5.2/sql-programming-guide.html#hive-tables
 *
 * + hive getting started
 * https://cwiki.apache.org/confluence/display/Hive/GettingStarted#GettingStarted-CreatingHiveTables
 *
 * + HiveContext not exist
 * http://stackoverflow.com/questions/26637210/error-while-using-hive-context-in-spark-object-hive-is-not-a-member-of-package
 *
 * More options: 从JSON文件加载数据; 连接既有Hive实例
 */
object Hive {

  case class Src(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    val hiveContext = new org.apache.spark.sql.hive.HiveContext(sc)
    import hiveContext._

    //hiveContext.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")

    // 创建DataFrame, 并注册为临时表
    val srcRDD = sc.parallelize(List(Src(1, "hello"), Src(2, "there")))
    val dataFrame = hiveContext.createDataFrame(srcRDD)
    dataFrame.registerTempTable("src")

    // 执行查询
    hiveContext.sql("FROM src SELECT key, value").collect().foreach(println)

    
    // 从JSON文件加载数据
    val tweets = hiveContext.read.json(Datasets.DATA_DIR + "tweets-sample.json")
    tweets.registerTempTable("tweets")
    hiveContext.sql("SELECT user.name, text FROM tweets").collect().foreach(println)
  }
}