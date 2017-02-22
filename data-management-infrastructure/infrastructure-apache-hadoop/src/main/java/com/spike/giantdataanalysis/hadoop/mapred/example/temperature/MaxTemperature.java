package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * MaxTemperature应用启动类<br>
 * (1) 直接以Java应用程序运行<br>
 * (2) 提交给集群<br>
 * <br>
 * <code>
 * export HADOOP_CLASSPATH=target/hadoop-mapred-0.0.1-SNAPSHOT.jar<br>
 * hadoop com.spike.hadoop.mapred.temperature.MaxTemperature input output
 * </code>
 * @author zhoujiagen
 */
public class MaxTemperature {

  public static void main(String[] args) throws Exception {
    Job job = Job.getInstance();
    job.setJarByClass(MaxTemperature.class);
    job.setJobName("Max Temperature");

    FileInputFormat.addInputPath(job, new Path("input"));
    FileOutputFormat.setOutputPath(job, new Path("output"));

    job.setMapperClass(MaxTemperatureMapper.class);
    job.setCombinerClass(MaxTemperatureReducer.class);
    job.setReducerClass(MaxTemperatureReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
