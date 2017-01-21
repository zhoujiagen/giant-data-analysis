package com.spike.giantdataanalysis.spark

/**
 * <pre>
 * 1 样例的两种运行方式:
 *
 * (1) 提交任务
 * 见scripts目录下的*.sh
 *
 * (2) 本地运行
 * val conf = new SparkConf().setMaster("local").setAppName("<appname>")
 * val sc = new SparkContext(conf)
 *
 *
 * 2 样例说明
 * 
 * ConsoleLogMining.scala 									文本中日志挖掘 - core
 * WordCount.scala													文本中单词计数 - core
 *
 * JsonHandleExample.scala									SQL JSON文件处理 - sql
 *
 * PrintStreamLogError.scala								输出日志流中错误 - streaming
 *
 * DistributedBlockMatrixExample.scala			BlockMatrix的示例 - mllib
 * DistributedCoordinateMatrixExample.scala	CoordinateMatrix的示例 - mllib
 * DistributedIndexedRowMatrixExample.scala	IndexedRowMatrix的示例 - mllib
 * DistributedRowMatrixExample.scala				RowMatrix示例 - mllib
 * LabeledPointExample.scala								带标签的点示例 - mllib
 * LocalMatrixExample.scala									本地矩阵示例 - mllib
 * LocalVectorExample.scala									本地向量示例 - mllib
 *
 * </pre>
 */
package object example {
  /**数据目录*/
  val DATA_DIR: String = "/Users/jiedong/github_local/giant-data-analysis/data-management-infrastructure/scala-infrastructure-apache-spark/data/"
}