package com.spike.giantdataanalysis.spark.example.mllib
/**
 * <pre>
 * RowMatrix示例
 *
 * 在sbt shell中运行：run vector.DistributedRowMatrixExample
 *
 * @author zhoujiagen
 */
object DistributedRowMatrixExample extends App {

  // 获取Spark上下文
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  val conf = new SparkConf().setMaster("local").setAppName("Spark MLlib Example")
  val sc = new SparkContext(conf)

  // 模拟数据
  import org.apache.spark.rdd.RDD
  val rawData: Array[Double] = Array(1, 2, 3, 4, 5)
  val rawRDDData: RDD[Double] = sc.parallelize(rawData)

  import org.apache.spark.mllib.linalg.{ Vector, Vectors }
  import org.apache.spark.mllib.linalg.distributed.RowMatrix

  // 创建
  val rows: RDD[Vector] = rawRDDData.map { data ⇒ Vectors.dense(rawData) }
  val mat: RowMatrix = new RowMatrix(rows)
  println("rows=" + rows)

  // 行和列数
  val m = mat.numRows()
  val n = mat.numCols()
  println(m)
  println(n)

  // QR分解
  val qrResult = mat.tallSkinnyQR(true)
  println(qrResult)
}