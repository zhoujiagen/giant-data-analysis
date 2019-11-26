package com.spike.giantdataanalysis.hudi.example


import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.QuickstartUtils._
import org.apache.hudi.config.HoodieWriteConfig._
import org.apache.spark.sql.SaveMode._

import scala.collection.JavaConversions._


object ExampleHudiWithSpark {

  val tableName = "hudi_cow_table"
  val basePath = "file:///tmp/hudi_cow_table"
  val dataGen = new DataGenerator

  def main(args: Array[String]): Unit = {
    println("insertData" + "*" * 100)
    insertData()
    println("queryDate, insertData" + "*" * 100)
    queryDate()

    println("updateData" + "*" * 100)
    updateData()
    println("queryDate, updateData" + "*" * 100)
    queryDate()

    println("incrementalQuery" + "*" * 100)
    incrementalQuery()
    println("pointInTimeQuery" + "*" * 100)
    pointInTimeQuery()
  }

  def insertData(): Unit = {
    val inserts = convertToStringList(dataGen.generateInserts(10))
    println("inserts=" + inserts.mkString(System.lineSeparator()))
    val df = ss.read.json(ss.sparkContext.parallelize(inserts, 2))
    df.write.format("org.apache.hudi")
      .options(getQuickstartWriteConfigs)
      .option(PRECOMBINE_FIELD_OPT_KEY, "ts")
      .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
      .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
      .option(TABLE_NAME, tableName)
      .mode(Overwrite)
      .save(basePath)
  }


  def queryDate(): Unit = {
    val roViewDF = ss.read.
      format("org.apache.hudi").
      load(basePath + "/*/*/*/*")
    roViewDF.registerTempTable("hudi_ro_table")
    ss.sql("select fare, begin_lon, begin_lat, ts from  hudi_ro_table where fare > 20.0").show()
    ss.sql("select _hoodie_commit_time, _hoodie_record_key, _hoodie_partition_path, rider, driver, fare from  hudi_ro_table").show()
  }

  def updateData(): Unit = {
    val updates = convertToStringList(dataGen.generateUpdates(10))
    println("updates=" + updates.mkString(System.lineSeparator()))
    val df = ss.read.json(ss.sparkContext.parallelize(updates, 2))
    df.write.format("org.apache.hudi")
      .options(getQuickstartWriteConfigs)
      .option(PRECOMBINE_FIELD_OPT_KEY, "ts")
      .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
      .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
      .option(TABLE_NAME, tableName)
      .mode(Append)
      .save(basePath)
  }

  def incrementalQuery(): Unit = {
    import ss.implicits._
    val commits: Array[String] = ss
      .sql("select distinct(_hoodie_commit_time) as commitTime from  hudi_ro_table order by commitTime")
      .map(k => k.getString(0)).take(50)
    println("commits=" + commits.mkString(System.lineSeparator()))

    val beginTime = commits(commits.length - 2) // commit time we are interested in

    // incrementally query data
    val incViewDF = ss.read
      .format("org.apache.hudi")
      .option(VIEW_TYPE_OPT_KEY, VIEW_TYPE_INCREMENTAL_OPT_VAL)
      .option(BEGIN_INSTANTTIME_OPT_KEY, beginTime)
      .load(basePath)
    incViewDF.registerTempTable("hudi_incr_table")
    ss.sql("select `_hoodie_commit_time`, fare, begin_lon, begin_lat, ts from  hudi_incr_table where fare > 20.0").show()
  }


  def pointInTimeQuery(): Unit = {
    import ss.implicits._
    val commits: Array[String] = ss
      .sql("select distinct(_hoodie_commit_time) as commitTime from  hudi_ro_table order by commitTime")
      .map(k => k.getString(0)).take(50)
    println("commits=" + commits.mkString(System.lineSeparator()))

    val beginTime = "000"
    // Represents all commits > this time.
    val endTime = commits(commits.length - 2) // commit time we are interested in

    //incrementally query data
    val incViewDF = ss.read
      .format("org.apache.hudi")
      .option(VIEW_TYPE_OPT_KEY, VIEW_TYPE_INCREMENTAL_OPT_VAL)
      .option(BEGIN_INSTANTTIME_OPT_KEY, beginTime)
      .option(END_INSTANTTIME_OPT_KEY, endTime)
      .load(basePath)
    incViewDF.registerTempTable("hudi_incr_table")
    ss.sql("select `_hoodie_commit_time`, fare, begin_lon, begin_lat, ts from  hudi_incr_table where fare > 20.0").show
  }

}
