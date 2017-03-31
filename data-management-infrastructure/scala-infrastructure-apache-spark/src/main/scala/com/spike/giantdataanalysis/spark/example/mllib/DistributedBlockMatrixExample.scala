package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * BlockMatrix的示例
 *
 * 在sbt shell中运行：run vector.DistributedBlockMatrixExample
 *
 * @author zhoujiagen
 */
object DistributedBlockMatrixExample extends App {

  // 获取Spark上下文
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  val conf = new SparkConf().setMaster("local").setAppName("Spark MLlib Example")
  val sc = new SparkContext(conf)

  // 模拟数据
  import org.apache.spark.rdd.RDD
  val rawData: Array[Double] = Array(1, 2, 3, 4, 5)
  val rawRDDData: RDD[Double] = sc.parallelize(rawData)

  import org.apache.spark.mllib.linalg.distributed.{ BlockMatrix, CoordinateMatrix, MatrixEntry }

  var i: Long = -1
  var j: Long = -1
  var value: Double = 0.0
  // Each entry is a tuple of (i: Long, j: Long, value: Double), where i is the row index, j is the column index, and value is the entry value. 
  //  MatrixEntry is a wrapper over (Long, Long, Double)
  val entries: RDD[MatrixEntry] = rawRDDData.map { data ⇒ MatrixEntry(i + 1, j + 1, i + j) }

  // 创建CoordinateMatrix
  val coordMat: CoordinateMatrix = new CoordinateMatrix(entries)

  // 将CoordinateMatrix转换成BlockMatrix
  // MatrixBlock is a tuple of ((Int, Int), Matrix), where the (Int, Int) is the index of the block, and Matrix is the sub-matrix at the given index with size rowsPerBlock x colsPerBlock.
  val matA: BlockMatrix = coordMat.toBlockMatrix().cache()

  // 验证BlockMatrix是否被合理的设置
  matA.validate()

  // 计算 A^T A.
  val ata = matA.transpose.multiply(matA)
  println(ata)
}