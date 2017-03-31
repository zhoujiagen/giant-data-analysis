package com.spike.giantdataanalysis.spark.example.data

import java.sql.DriverManager
import java.sql.ResultSet
import org.apache.spark.rdd.JdbcRDD

object MySQL {

  // MySQL Connector/J URL syntax
  // https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
  def getConnection() = {
    Class.forName("com.mysql.jdbc.Driver").newInstance()
    DriverManager.getConnection("jdbc:mysql://localhost:3306/svn?user=root&password=&useUnicode=true&characterEncoding=utf8")
  }

  // start from 1
  def mapRow(rs: ResultSet) = (rs.getLong(1), rs.getString(2))

  def main(args: Array[String]): Unit = {
    // sc: org.apache.spark.SparkContext, getConnection: () ⇒ java.sql.Connection,
    // sql: String, lowerBound: Long, upperBound: Long, 
    // numPartitions: Int, mapRow: java.sql.ResultSet ⇒ T
    val data = new JdbcRDD(sc, getConnection,
      "select id, version from SVN where id >= ? and id <= ?",
      lowerBound = 250, upperBound = 300, numPartitions = 2, mapRow)
    data.collect().foreach(println)
  }
}