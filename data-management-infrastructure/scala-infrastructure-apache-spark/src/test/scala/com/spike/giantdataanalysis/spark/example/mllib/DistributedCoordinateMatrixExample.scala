package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * CoordinateMatrix的示例
 *
 * 在sbt shell中运行：run vector.DistributedCoordinateMatrixExample
 *
 * @author zhoujiagen
 */
object DistributedCoordinateMatrixExample extends App {

  // 获取Spark上下文
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  val conf = new SparkConf().setMaster("local").setAppName("Spark MLlib Example")
  val sc = new SparkContext(conf)

  // 模拟数据
  import org.apache.spark.rdd.RDD
  val rawData: Array[Double] = Array(1, 2, 3, 4, 5)
  val rawRDDData: RDD[Double] = sc.parallelize(rawData)

  import org.apache.spark.mllib.linalg.distributed.{ CoordinateMatrix, MatrixEntry }

  // 创建
  var i: Long = -1
  var j: Long = -1
  var value: Double = 0.0
  // Each entry is a tuple of (i: Long, j: Long, value: Double), where i is the row index, j is the column index, and value is the entry value. 
  //  MatrixEntry is a wrapper over (Long, Long, Double)
  val entries: RDD[MatrixEntry] = rawRDDData.map { data ⇒ MatrixEntry(i + 1, j + 1, i + j) }
  val mat: CoordinateMatrix = new CoordinateMatrix(entries)

  // Get its size.
  val m = mat.numRows()
  val n = mat.numCols()
  println(m)
  println(n)

  val indexedRowMatrix = mat.toIndexedRowMatrix()

}