package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * 带标签的点示例
 *
 * 在sbt shell中运行：run
 *
 * @author zhoujiagen
 */
object LabeledPointExample extends App {

  import org.apache.spark.mllib.linalg.Vectors
  import org.apache.spark.mllib.regression.LabeledPoint

  // 稠密向量，正标签
  val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
  println(pos)

  // 稀疏向量，负标签
  val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
  println(neg)

  // 从LIBSVM格式文件中读取训练样本
  import org.apache.spark.mllib.util.MLUtils
  import org.apache.spark.rdd.RDD

  // 获取Spark上下文
  import org.apache.spark.SparkConf
  import org.apache.spark.SparkContext
  val conf = new SparkConf().setMaster("local").setAppName("Spark MLlib Example")
  val sc = new SparkContext(conf)

  val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, DATA_DIR + "data.libsvm")
  println(examples)
}
