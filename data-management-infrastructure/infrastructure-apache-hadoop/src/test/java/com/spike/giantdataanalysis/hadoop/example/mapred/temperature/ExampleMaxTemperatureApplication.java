package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

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
 * 
 * @author zhoujiagen
 * @see ExampleMaxTemperatureMapper
 * @see ExampleMaxTemperatureReducer
 */
public class ExampleMaxTemperatureApplication {

  public static void main(String[] args) throws Exception {
    Hadoops.setUpEnvironment();

    Job job = Job.getInstance();
    job.setJarByClass(ExampleMaxTemperatureApplication.class);
    job.setJobName("Max Temperature");

    // only for test
    Path outputPath = new Path(ExampleConstants.DATA_NCDC_OUTPUT_PATH);
    Configuration conf = new Configuration();
    Hadoops.deletePath(conf, outputPath, true);

    FileInputFormat.addInputPath(job, new Path(ExampleConstants.DATA_NCDC_INPUT_PATH));
    FileOutputFormat.setOutputPath(job, outputPath);

    job.setMapperClass(ExampleMaxTemperatureMapper.class);
    // 使用Combiner
    // job.setCombinerClass(MaxTemperatureReducer.class);
    job.setReducerClass(ExampleMaxTemperatureReducer.class);

    // map的输出与reduce的输出一致时可以不设置
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
