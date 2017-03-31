package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * 本地向量示例
 *
 * 在sbt shell中运行：run
 *
 * @author zhoujiagen
 */
object LocalVectorExample extends App {

  import org.apache.spark.mllib.linalg.{ Vector, Vectors }

  // 稠密向量
  val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
  println(dv)

  // 稀疏向量
  // 向量长度，索引数组，值数组
  val sv1: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
  println(sv1)
  // 向量长度，索引-值的序列
  val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))
  println(sv2)
}
