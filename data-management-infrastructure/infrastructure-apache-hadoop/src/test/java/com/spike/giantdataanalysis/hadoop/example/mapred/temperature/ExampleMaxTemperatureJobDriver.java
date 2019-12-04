package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * Job Driver of max temperature
 * @author zhoujiagen
 * @see ExampleMaxTemperatureMapper
 * @see ExampleMaxTemperatureReducer
 */
public class ExampleMaxTemperatureJobDriver extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    Hadoops.setUpEnvironment();

    int exitCode = ToolRunner.run(new ExampleMaxTemperatureJobDriver(), args);

    System.exit(exitCode);
  }

  @Override
  public int run(String[] args) throws Exception {

    // 输出通用命令
    ToolRunner.printGenericCommandUsage(System.err);

    // 基于Tool的配置创建作业
    Job job = Job.getInstance(super.getConf(), "Max temperature");
    job.setJarByClass(getClass());

    // for local test
    // FileInputFormat.addInputPath(job, new Path(ExampleConstants.DATA_NCDC_INPUT_PATH));
    // FileOutputFormat.setOutputPath(job, new Path(ExampleConstants.DATA_NCDC_OUTPUT_PATH));

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setMapperClass(ExampleMaxTemperatureMapper.class);
    job.setCombinerClass(ExampleMaxTemperatureReducer.class);
    job.setReducerClass(ExampleMaxTemperatureReducer.class);

    // 默认的输入格式TextInputFormat, [LongWritable, Text]
    job.setInputFormatClass(TextInputFormat.class);

    // map的输出与reduce的输出一致时可以不设置
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    return job.waitForCompletion(true) ? 0 : 1;
  }

}
