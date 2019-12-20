package com.spike.giantdataanalysis.hadoop.example.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.AccessControlException;

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * 文件系统示例
 * @author zhoujiagen
 */
public class ExampleFileSystem {

  public static void main(String[] args) throws IOException {
    local(args);
    remote(args);
  }

  public static void local(String[] args) throws IOException {
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

  public static void remote(String[] args) throws IOException {
    /**
     * walk around: org.apache.hadoop.hdfs.DFSClient.primitiveMkdir(String, FsPermission,
     * boolean)时修改DFSClient.ugi的fullName值
     */

    Hadoops.setUpEnvironment();
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "hdfs://localhost:9000");
    // conf.set("fs.permissions.umask-mode", "000");
    FileSystem fs = FileSystem.get(conf);
    Path home = new Path("/user");
    fs.mkdirs(home, FsPermission.getDirDefault());
    Path pwd = new Path("/user/root");
    fs.mkdirs(pwd);
  }

}
