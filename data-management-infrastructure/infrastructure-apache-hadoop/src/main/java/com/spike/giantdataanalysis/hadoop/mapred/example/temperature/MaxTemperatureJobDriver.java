package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Job Driver of max temperature
 * @author zhoujiagen
 * @see MaxTemperatureMapper
 * @see MaxTemperatureReducer
 */
public class MaxTemperatureJobDriver extends Configured implements Tool {

  // static {
  // Configuration.addDefaultResource("conf/hadoop-localhost.xml");
  // }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new MaxTemperatureJobDriver(), args);

    System.exit(exitCode);
  }

  @Override
  public int run(String[] args) throws Exception {

    // 输出通用命令
    ToolRunner.printGenericCommandUsage(System.err);

    Job job = Job.getInstance(getConf(), "Max temperature");
    job.setJarByClass(getClass());

    FileInputFormat.addInputPath(job, new Path("data/input"));
    FileOutputFormat.setOutputPath(job, new Path("data/output"));

    job.setMapperClass(MaxTemperatureMapper.class);
    job.setCombinerClass(MaxTemperatureReducer.class);
    job.setReducerClass(MaxTemperatureReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    return job.waitForCompletion(true) ? 0 : 1;
  }

}
