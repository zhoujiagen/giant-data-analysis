package com.spike.giantdataanalysis.flink.example.table

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java
import org.apache.flink.api.java.io.CsvInputFormat
import org.apache.flink.api.scala._
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._
import org.apache.flink.table.sinks.{BatchTableSink, TableSinkBase}
import org.apache.flink.table.sources.CsvTableSource
import org.apache.flink.types.Row

/**
  * Table API示例.
  */
object ExampleTableCSV {
  def main(args: Array[String]): Unit = {
    // 1 create Table environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)

    // 2 load data from csv table source
    val customerSource = new CsvTableSource(
      "data/Customer.csv", // path
      Array("ID", "NAME", "D_ID"), // fieldNames
      Array(createTypeInformation[Long], //fieldTypes
        createTypeInformation[String],
        createTypeInformation[Long]),
      CsvInputFormat.DEFAULT_FIELD_DELIMITER, CsvInputFormat.DEFAULT_LINE_DELIMITER,
      null, true, null, false)
    tableEnv.registerTableSource("customer", customerSource)

    val districtSource = new CsvTableSource(
      "data/District.csv",
      Array("ID", "NAME"),
      Array(createTypeInformation[Long],
        createTypeInformation[String]),
      CsvInputFormat.DEFAULT_FIELD_DELIMITER, CsvInputFormat.DEFAULT_LINE_DELIMITER,
      null, true, null, false)
    tableEnv.registerTableSource("district", districtSource)

    // 3 execute sql query
    val customers = tableEnv.sqlQuery(
      """
        |SELECT ID, NAME FROM customer
      """.stripMargin)

    // 4 write to table sink
    // JDBC
    //    val jdbcSink: JDBCAppendTableSink = JDBCAppendTableSink.builder()
    //      .setDrivername("com.mysql.jdbc.Driver")
    //      .setDBUrl("jdbc:mysql://localhost:3306/flink")
    //      .setQuery("INSERT INTO Customer(ID,NAME,D_ID) VALUES(?,?,?)")
    //      .setUsername("root").setPassword("admin")
    //      .setParameterTypes(createTypeInformation[Long], createTypeInformation[String], createTypeInformation[Long])
    //      .build()
    //    customers.writeToSink(jdbcSink)
    // DEBUG
    val debugSink = new DummyBatchTableSink
    customers.writeToSink(debugSink)

    // execute sql query with table api
    val customerTable = tableEnv.scan("customer").select('ID as 'cid, 'NAME as 'cname, 'D_ID)
    val districtTable = tableEnv.scan("district").select('ID as 'did, 'NAME as 'dname)
    customerTable.join(districtTable).where('D_ID === 'did).select('cname, 'dname)
      .writeToSink(debugSink)

    // 5 trigger execution
    env.execute()
  }
}


// SINK FOR DEBUG
class DummyBatchTableSink extends TableSinkBase[Row] with BatchTableSink[Row] {
  override def getOutputType: TypeInformation[Row] = createTypeInformation[Row]

  override def emitDataSet(dataSet: java.DataSet[Row]): Unit = {
    dataSet.print()
    //println(dataSet.collect())
  }

  override protected def copy: TableSinkBase[Row] = new DummyBatchTableSink()
}