package com.spike.giantdataanalysis.hadoop.types;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.hadoop.support.ApplicationConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * {@link SequenceFile}的示例
 * @author zhoujiagen
 */
public class SequenceFileExample {
  private static final String[] DATA = { //
      "One, two, buckle my shoe", //
      "Three, four, shut the door", //
      "Five, six, pick up sticks", //
      "Seven, eight, lay them straight", //
      "Nine, ten, a big fat hen" };

  public static void main(String[] args) throws IOException {
    String pathUri = ApplicationConstants.DATA_TEST_OUTPUT_PATH + "/seqfile";
    if (args.length > 0 && StringUtils.isNotBlank(args[0])) {
      pathUri = args[0];
    }

    Configuration conf = new Configuration();
    Path path = new Path(ApplicationConstants.DATA_TEST_OUTPUT_PATH);
    Hadoops.DELETE_LOCAL(conf, path, true);
    path = new Path(pathUri);

    write(conf, path);
    readAll(conf, path);
    readSeek(conf, path);
    sort(conf, path);
  }

  static void write(Configuration conf, Path path) throws IOException {

    IntWritable key = new IntWritable();
    Text value = new Text();

    SequenceFile.Writer.Option[] options = new SequenceFile.Writer.Option[] { //
        SequenceFile.Writer.file(path), //
        SequenceFile.Writer.keyClass(key.getClass()), //
        SequenceFile.Writer.valueClass(value.getClass()) //
    };

    // or IOUtils.closeStream(writer);
    try (SequenceFile.Writer writer = SequenceFile.createWriter(conf, options);) {
      for (int i = 0; i < 100; i++) {
        key.set(100 - i);
        value.set(DATA[i % DATA.length]);
        // 写入位置, 键, 值
        System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
        writer.append(key, value);
      }
    }

  }

  static void readAll(Configuration conf, Path path) throws IOException {
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

  static void readSeek(Configuration conf, Path path) throws IOException {

    SequenceFile.Reader.Option[] options = new SequenceFile.Reader.Option[] { //
        SequenceFile.Reader.file(path) //
    };
    try (SequenceFile.Reader reader = new SequenceFile.Reader(conf, options);) {

      Writable _key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
      Writable _value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

      // 在记录边界
      // [359] 95 One, two, buckle my shoe
      reader.seek(359);
      Preconditions.checkState(reader.next(_key, _value));

      // 不在记录边界
      reader.seek(360);
      try {
        reader.next(_key, _value);
      } catch (IOException e) {
        System.err.println("seek fail");
      }

      // 访问位置后的同步点
      reader.sync(360);
      Preconditions.checkState(2021L == reader.getPosition());
      Preconditions.checkState(reader.next(_key, _value));
    }
  }

  /**
   * 排序
   * @param conf
   * @param path
   * @throws IOException
   * @see {@link SequenceFile.Sorter#sort(Path, Path)}
   * @see SequenceFile.Sorter#merge(Path[], Path)
   */
  static void sort(Configuration conf, Path path) throws IOException {

    IntWritable key = new IntWritable();
    Text value = new Text();
    FileSystem fs = FileSystem.get(path.toUri(), conf);
    SequenceFile.Sorter sorter =
        new SequenceFile.Sorter(fs, key.getClass(), value.getClass(), conf);

    Path output = new Path(path.toString() + "_sorted");
    sorter.sort(path, output);

    Hadoops.DEV_RENDER_WRITABLE_SEQFILE(conf, output);
  }
}
