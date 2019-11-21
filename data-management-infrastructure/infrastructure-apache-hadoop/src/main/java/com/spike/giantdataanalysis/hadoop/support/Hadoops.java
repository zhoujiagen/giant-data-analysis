package com.spike.giantdataanalysis.hadoop.support;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Hadoops {

  private static final Logger LOG = LoggerFactory.getLogger(Hadoops.class);

  public static final String HADOOP_HOME_VALUE = "/Users/jiedong/software/hadoop-2.7.2";

  public static final String HADOOP_HOME_PROP_NAME = "hadoop.home.dir";
  public static final String HADOOP_HOME_ENV_NAME = "HADOOP_HOME";

  /**
   * 设置系统/环境属性
   */
  public static void SETUP_ENV() {
    String home = System.getProperty(HADOOP_HOME_PROP_NAME);
    if (home == null) {
      System.setProperty(HADOOP_HOME_PROP_NAME, HADOOP_HOME_VALUE);
      // home = System.getenv(HADOOP_HOME_ENV_NAME);
    }
  }

  /**
   * 输出配置内容
   * @param conf
   */
  public static void RENDER(Configuration conf) {
    if (conf == null) return;

    StringBuilder sb = new StringBuilder();
    sb.append("\n" + StringUtils.repeat("=", 50) + "\n");
    sb.append("Configuration\n");
    sb.append(StringUtils.repeat("-", 50) + "\n");

    // 这种方式变量不会展开
    // Iterator<Entry<String, String>> it = conf.iterator();
    // Entry<String, String> elem;
    // while (it.hasNext()) {
    // elem = it.next();
    // sb.append(elem.getKey() + "=" + elem.getValue() + "\n");
    // }

    // 支持变量展开
    Map<String, String> map = conf.getValByRegex(".*");
    TreeMap<String, String> treeMap = new TreeMap<String, String>(map);
    for (String key : treeMap.keySet()) {
      sb.append(key + "=" + treeMap.get(key) + "\n");
    }

    sb.append(StringUtils.repeat("=", 50) + "\n");

    // System.out.print(sb.toString());
    LOG.info(sb.toString());
  }

  /**
   * 删除路径
   * @param conf
   * @param path
   * @param recursive 是否递归删除
   * @throws IOException
   * @see {@link FileSystem#get(Configuration)}
   */
  public static void DELETE(Configuration conf, Path path, boolean recursive) throws IOException {
    if (conf == null || path == null) return;

    FileSystem fs = FileSystem.get(conf);
    fs.delete(path, recursive);
  }

  /**
   * 删除本地文件路径
   * @param conf
   * @param path
   * @param recursive 是否递归删除
   * @throws IOException
   * @see {@link FileSystem#getLocal(Configuration)}
   */
  public static void DELETE_LOCAL(Configuration conf, Path path, boolean recursive)
      throws IOException {
    if (conf == null || path == null) return;

    FileSystem fs = FileSystem.getLocal(conf);
    fs.delete(path, recursive);
  }

  public static void DELETE_LOCAL(String path, boolean recursive) throws IOException {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.getLocal(conf);
    fs.delete(new Path(path), recursive);
  }

  /**
   * 最小的MapReduce作业
   * @author zhoujiagen
   */
  public static class MinimalMapReduce extends Configured implements Tool {

    // example Arguments: data/ncdc/input data/ncdc/output
    //
    // more argument parse REF:
    // https://github.com/tomwhite/hadoop-book/blob/master/common/src/main/java/JobBuilder.java
    public static void main(String[] args) {
      if (args.length != 2) {
        ToolRunner.printGenericCommandUsage(System.out);
      }

      int exitCode = 0;
      try {
        exitCode = ToolRunner.run(new MinimalMapReduce(), args);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
      }
      System.exit(exitCode);
    }

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
      job.setMapperClass(Mapper.class);
      job.setMapOutputKeyClass(LongWritable.class);
      job.setMapOutputValueClass(Text.class);

      // partitioner
      // 每个分区由一个reduce任务处理
      job.setPartitionerClass(HashPartitioner.class);

      // reducer
      job.setNumReduceTasks(1); // 任务数量
      job.setReducerClass(Reducer.class);
      job.setOutputKeyClass(LongWritable.class);
      job.setOutputValueClass(Text.class);

      return job.waitForCompletion(true) ? 0 : 1;
    }

  }

  /**
   * 输出使用{@link Writable}序列化的{@link SequenceFile}中内容
   * @param conf
   * @param path
   * @throws IOException
   */
  public static void DEV_RENDER_WRITABLE_SEQFILE(Configuration conf, Path path) throws IOException {

    SequenceFile.Reader.Option[] options = new SequenceFile.Reader.Option[] { //
        SequenceFile.Reader.file(path) //
    };
    try (SequenceFile.Reader reader = new SequenceFile.Reader(conf, options);) {

      Writable _key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
      Writable _value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

      long position = reader.getPosition();

      // 使用内建的Writable的遍历方式
      while (reader.next(_key, _value)) {
        String syncSeen = reader.syncSeen() ? "*" : "";
        System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, _key, _value);

        position = reader.getPosition();
      }
    }
  }

}
