package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapred.ClusterMapReduceTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hadoop.support.ApplicationConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * <pre>
 * 在mini集群中测试
 * 
 * REF tomwhite/hadoop-book: https://github.com/tomwhite/hadoop-book
 * ch06-mr-dev/src/test/java/v2/MaxTemperatureDriverMiniTest.java
 * 
 * TODO failed when launch Yarn AM. fix it when processing Yarn.
 * </pre>
 * 
 * @author zhoujiagen
 * @see org.apache.hadoop.fs.FileUtil
 * @see org.apache.hadoop.fs.FileStatus
 * @see org.apache.hadoop.hdfs.MiniDFSCluster
 */
public class MaxTemperatureJobDriverMiniClusterTest extends ClusterMapReduceTestCase {

  private static final Logger LOG =
      LoggerFactory.getLogger(MaxTemperatureJobDriverMiniClusterTest.class);

  @Test
  public void setUp() throws Exception {
    // Hadoops.SETUP_ENV();
    super.setUp();
  }

  @Test
  public void test() throws Exception {
    Configuration conf = super.createJobConf();
    Hadoops.RENDER(conf);

    Path localInput = new Path(ApplicationConstants.DATA_NCDC_INPUT_PATH);
    // 集群中输入和输出目录
    Path input = super.getInputDir();
    Path output = super.getOutputDir();
    // 1 创建HDFS输入目录, 拷贝数据到测试HDFS
    FileSystem fs = super.getFileSystem();
    // fs.mkdirs(input);
    fs.copyFromLocalFile(localInput, input);
    LOG.info("localInput={}, input={}, output={}", localInput, input, output);

    // 2 运行作业
    MaxTemperatureJobDriver driver = new MaxTemperatureJobDriver();
    driver.setConf(conf);
    int exitCode = driver.run(new String[] { input.toString(), output.toString() });
    LOG.info("exitCode = {}", exitCode);
    Assert.assertEquals(0, exitCode);

    // 3 展示输出
    PathFilter filter = new PathFilter() {
      @Override
      public boolean accept(Path path) {
        return !path.getName().startsWith("_");
      }
    };
    Path[] outputFiles = FileUtil.stat2Paths(fs.listStatus(output, filter));
    try (InputStream is = fs.open(outputFiles[0]); //
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        LOG.info(line);
      }
    }
  }
}
