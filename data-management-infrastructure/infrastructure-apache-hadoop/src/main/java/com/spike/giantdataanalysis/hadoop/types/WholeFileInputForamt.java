package com.spike.giantdataanalysis.hadoop.types;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 将整个文件作为一条记录的{@link FileInputFormat}
 * @author zhoujiagen
 * @see FileSplit
 * @see IOUtils#readFully(java.io.InputStream, byte[], int, int)
 */
public class WholeFileInputForamt extends FileInputFormat<NullWritable, BytesWritable> {

  @Override
  public boolean isSplitable(JobContext context, Path filename) {
    return false;
  }

  @Override
  public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split,
      TaskAttemptContext context) throws IOException, InterruptedException {
    WholeFileRecordReader reader = new WholeFileRecordReader();
    reader.initialize(split, context);
    return reader;
  }

  // 记录读取器
  public static class WholeFileRecordReader extends RecordReader<NullWritable, BytesWritable> {

    private FileSplit fileSplit; // 文件分片
    private Configuration conf;
    private boolean processed = false;// 是否已读取标志
    private BytesWritable value = new BytesWritable();

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
        throws IOException, InterruptedException {
      this.fileSplit = (FileSplit) split;// cast
      this.conf = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
      if (!processed) {// 未处理过
        Path path = fileSplit.getPath();// 分片表示的文件路径
        byte[] content = new byte[(int) fileSplit.getLength()];
        FileSystem fs = path.getFileSystem(conf);

        try (FSDataInputStream is = fs.open(path);) {
          IOUtils.readFully(is, content, 0, content.length);
          value.set(content, 0, content.length);
        }
        processed = true;
        return true;
      }

      return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
      return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
      return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
      return processed ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {
      // do nothing
    }

  }

  /**
   * {@link WholeFileInputFormat}的用例
   * <p>
   * 将多个小文件合并为{@link SequenceFile}
   * <p>
   * 键: 文件名, 值: 整个文件中数据
   * @author zhoujiagen
   */
  public static class SmallFiles2SequenceFileJobDriver extends Configured implements Tool {

    public static class SmallFiles2SequenceFileMapper
        extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

      private Text fileNameKey;

      @Override
      public void setup(Context context) throws IOException, InterruptedException {
        // 从Mapper中获取Split信息
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        fileNameKey = new Text(fileSplit.getPath().toString());
      }

      @Override
      public void map(NullWritable key, BytesWritable value, Context context)
          throws IOException, InterruptedException {
        context.write(fileNameKey, value);
      }
    }

    public static void main(String[] args) throws Exception {
      System.exit(ToolRunner.run(new SmallFiles2SequenceFileJobDriver(), args));
    }

    @Override
    public int run(String[] args) throws Exception {

      Job job = Job.getInstance(super.getConf());
      job.setJarByClass(getClass());
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));

      // 默认配置
      // format
      job.setInputFormatClass(WholeFileInputForamt.class);
      job.setOutputFormatClass(SequenceFileOutputFormat.class);

      // mapper
      // mapper任务的数量等于输入拆分为split的数量
      // split的数量由输入大小和文件块的大小(如果在HDFS上)决定
      job.setMapperClass(SmallFiles2SequenceFileMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(BytesWritable.class);

      // partitioner
      // 每个分区由一个reduce任务处理
      job.setPartitionerClass(HashPartitioner.class);

      // reducer
      job.setNumReduceTasks(2); // 任务数量
      job.setReducerClass(Reducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(BytesWritable.class);

      return job.waitForCompletion(true) ? 0 : 1;

    }

  }

}
