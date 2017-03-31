package com.spike.giantdataanalysis.spark.example.mllib

/**
 * <pre>
 * 本地矩阵示例
 *
 * 在sbt shell中运行：run
 *
 * @author zhoujiagen
 */
object LocalMatrixExample extends App {

  import org.apache.spark.mllib.linalg.{ Matrix, Matrices }

  // 稠密矩阵
  // 1.0 2.0
  // 3.0 4.0
  // 5.0 6.0
  val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
  println(dm)

  // 稀疏矩阵
  // CSC压缩： http://www.cnblogs.com/xbinworld/p/4273506.html
  // 9.0 0.0
  // 0.0 8.0
  // 0.0 6.0
  // 元素之非零元素
  // column offset:     [0      1   3   ]    某列的一个元素在values中的偏移量，最后补上总元素数量
  // row index:             [0     2   1   ]    元素所在的行
  // values:                   [9.0 6.0 8.0]   元素值
  // OUTPUT
  // 3 x 2 CSCMatrix
  // (0,0) 9.0
  // (2,1) 6.0
  // (1,1) 8.0
  val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))
  println(sm)
}
