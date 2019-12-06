package com.spike.giantdataanalysis.hadoop.example.hdfs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.security.AccessControlException;

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * 本地文件系统示例
 * @author zhoujiagen
 */
public class ExampleLocalFileSystem {
  public static void main(String[] args) throws IOException {
    Hadoops.setUpEnvironment();

    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.getLocal(conf); // local

    Path dirPath = new Path(ExampleConstants.DATA_NCDC_INPUT_PATH);
    Path filePath = new Path(ExampleConstants.DATA_NCDC_INPUT_PATH + "/1901");
    System.out.println(fs.isDirectory(dirPath));
    System.out.println(fs.isDirectory(filePath));

    FsStatus fsStatus = fs.getStatus(dirPath);
    final double factor_gb = 1024.0 * 1024.0 * 1024.0;
    final double factor_mb = 1024.0 * 1024.0;
    System.out.println(fsStatus.getCapacity() / factor_gb);
    System.out.println(fsStatus.getUsed() / factor_gb);
    System.out.println(fsStatus.getRemaining() / factor_gb);
    try {
      fs.access(filePath, FsAction.READ_WRITE);
    } catch (AccessControlException e) {
      e.printStackTrace();
    }

    FileStatus dirPathStatus = fs.getFileStatus(dirPath);
    System.out.println(dirPathStatus);
    FileStatus filePathStatus = fs.getFileLinkStatus(filePath);
    System.out.println(filePathStatus);
    System.out.println(filePathStatus.getBlockSize() / factor_mb);
  }
}
