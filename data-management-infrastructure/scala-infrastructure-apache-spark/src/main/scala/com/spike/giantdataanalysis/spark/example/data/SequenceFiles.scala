package com.spike.giantdataanalysis.spark.example.data

import com.spike.giantdataanalysis.spark.support.Datasets
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.IntWritable

/**
 * 操作Hadoop SequenceFile
 */
object SequenceFiles {
  def main(args : Array[String]) : Unit = {
    // 保存
    val data = sc.parallelize(List(("Panda", 3), ("Kay", 6), ("Snail", 2)))
    // 确保目录不存在
    val path = Datasets.OUTPUT_DATA_DIR + "_SequenceFiles"
    data.saveAsSequenceFile(path)

    // 读取
    val data2 = sc.sequenceFile(path+"/part-00000", classOf[Text], classOf[IntWritable])
    data2.foreach(println)
  }
}