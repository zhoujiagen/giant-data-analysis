package com.spike.giantdataanalysis.spark.example.data

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat

import com.spike.giantdataanalysis.spark.support.Datasets
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.LongWritable

object HadoopFiles {
  def main(args : Array[String]) : Unit = {
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    val path = Datasets.OUTPUT_DATA_DIR + "hadoopFile"
    val hadoopConf = new Configuration()

    // 使用new API
    rdd.map { x ⇒ (new LongWritable(x), new Text(x.toString())) }
      .saveAsNewAPIHadoopFile(path,
        classOf[LongWritable], classOf[Text], classOf[TextOutputFormat[LongWritable, Text]],
        hadoopConf)

    val read = sc.newAPIHadoopFile(path,
      classOf[TextInputFormat], classOf[LongWritable], classOf[Text],
      hadoopConf)
    read.foreach(println)
  }
}