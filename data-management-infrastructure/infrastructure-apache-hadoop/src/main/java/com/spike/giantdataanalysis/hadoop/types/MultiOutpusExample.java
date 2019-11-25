package com.spike.giantdataanalysis.hadoop.types;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.spike.giantdataanalysis.hadoop.mapred.example.temperature.LineRecordParser;
import com.spike.giantdataanalysis.hadoop.support.ApplicationConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * {@link MultiOutputs}的示例
 * <p>
 * 将NCDC数据集按气象站拆分
 * @author zhoujiagen
 */
public class MultiOutpusExample {

  public static void main(String[] args) throws Exception {

    args = new String[2];
    args[0] = ApplicationConstants.DATA_NCDC_INPUT_PATH;
    args[1] = ApplicationConstants.DATA_NCDC_OUTPUT_PATH;
    Hadoops.DELETE_LOCAL(args[1], true);

    System.exit(ToolRunner.run(new Application(), args));
  }

  public static class Application extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
      Job job = Job.getInstance(super.getConf());

      job.setJarByClass(getClass());
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));

      // 默认配置
      // format
      job.setInputFormatClass(TextInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);

      // mapper
      // mapper任务的数量等于输入拆分为split的数量
      // split的数量由输入大小和文件块的大小(如果在HDFS上)决定
      job.setMapperClass(NCDCStationMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Text.class);

      // partitioner
      // 每个分区由一个reduce任务处理
      job.setPartitionerClass(HashPartitioner.class);

      // reducer
      // job.setNumReduceTasks(1); // 任务数量
      job.setReducerClass(NCDCStationReducer.class);
      job.setOutputKeyClass(NullWritable.class);
      job.setOutputValueClass(Text.class);

      return job.waitForCompletion(true) ? 0 : 1;
    }

  }

  public static class NCDCStationMapper extends Mapper<LongWritable, Text, Text, Text> {
    private LineRecordParser parser = new LineRecordParser();

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      parser.parse(value);
      // 键为气象站标识
      context.write(new Text(parser.getUSAFStationId()), value);
    }
  }

  public static class NCDCStationReducer extends Reducer<Text, Text, NullWritable, Text> {
    private MultipleOutputs<NullWritable, Text> multipleOutputs;

    public void setup(Context context) throws IOException, InterruptedException {
      multipleOutputs = new MultipleOutputs<NullWritable, Text>(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
      String baseOutputPath = key.toString();
      for (Text value : values) {
        // 不使用context.write(...)
        multipleOutputs.write(NullWritable.get(), value, baseOutputPath);
      }
    }
  }

}
