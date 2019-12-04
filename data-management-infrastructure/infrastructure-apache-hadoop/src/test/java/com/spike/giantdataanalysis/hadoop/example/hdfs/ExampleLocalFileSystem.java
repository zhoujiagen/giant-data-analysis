package com.spike.giantdataanalysis.hadoop.example.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * 本地文件系统示例
 * @author zhoujiagen
 */
public class ExampleLocalFileSystem {
  public static void main(String[] args) throws IOException {
    Hadoops.setUpEnvironment();

    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.getLocal(conf);

    Path inputPath = new Path("input");
    System.out.println(fs.isDirectory(inputPath));

  }
}
