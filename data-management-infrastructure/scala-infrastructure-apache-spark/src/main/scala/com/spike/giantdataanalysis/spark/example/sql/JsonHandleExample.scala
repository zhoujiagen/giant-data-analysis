package com.spike.giantdataanalysis.spark.example.mllib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * SQL JSON文件处理
 * @author zhoujiagen
 */
object JsonHandleExample {
  def main(args: Array[String]) {
    // configuration and context
    val conf = new SparkConf().setAppName("Spark SQL with Json Example")
    val sc = new SparkContext(conf)

    // sql context
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //used to implicitly convert an RDD to a DataFrame.
    import sqlContext.implicits._

    // create DataFrame
    val df = sqlContext.read.json(DATA_DIR + "people.json")

    // view schema
    df.printSchema()

    // DataFrame operations
    df.select("name").show()
    df.filter(df("age") > 21).show()
    df.groupBy("age").count().show()
  }
}