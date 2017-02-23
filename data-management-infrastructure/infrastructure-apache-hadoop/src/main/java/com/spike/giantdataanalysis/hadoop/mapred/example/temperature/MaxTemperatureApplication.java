package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * <pre>
 * MaxTemperature应用启动类
 * (1) 直接以Java应用程序运行
 * (2) 提交给集群
 * <code>
 * export HADOOP_CLASSPATH=target/xxx.jar
 * hadoop this-full-path-class-name input output
 * </code>
 * </pre>
 * @author zhoujiagen
 */
public class MaxTemperatureApplication {

  public static void main(String[] args) throws Exception {
    Job job = Job.getInstance();
    job.setJarByClass(MaxTemperatureApplication.class);
    job.setJobName("Max Temperature");

    FileInputFormat.addInputPath(job, new Path("input"));
    FileOutputFormat.setOutputPath(job, new Path("output"));

    job.setMapperClass(MaxTemperatureMapper.class);
    // 使用Combiner
    // job.setCombinerClass(MaxTemperatureReducer.class);
    job.setReducerClass(MaxTemperatureReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
