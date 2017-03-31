package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * IndexedRowMatrix的示例
 *
 * 在sbt shell中运行：run vector.DistributedIndexedRowMatrixExample
 *
 * @author zhoujiagen
 */
object DistributedIndexedRowMatrixExample extends App {

  // 获取Spark上下文
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  val conf = new SparkConf().setMaster("local").setAppName("Spark MLlib Example")
  val sc = new SparkContext(conf)

  // 模拟数据
  import org.apache.spark.rdd.RDD
  val rawData: Array[Double] = Array(1, 2, 3, 4, 5)
  val rawRDDData: RDD[Double] = sc.parallelize(rawData)

  import org.apache.spark.mllib.linalg.Vectors
  import org.apache.spark.mllib.linalg.distributed.{ IndexedRow, IndexedRowMatrix, RowMatrix }

  // 创建
  var i: Long = -1
  val rows: RDD[IndexedRow] = rawRDDData.map { data ⇒ IndexedRow(i + 1, Vectors.dense(rawData)) }
  val mat: IndexedRowMatrix = new IndexedRowMatrix(rows)
  println(rows)

  // 行列
  val m = mat.numRows()
  val n = mat.numCols()
  println(m)
  println(n)

  // 删除行索引
  val rowMat: RowMatrix = mat.toRowMatrix()
  println(rowMat)

}