package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

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

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * 在mini集群中测试.
 * <p>
 * 注意: 设置JAVA_HOME以解决YARN中未找到/bin/java问题.
 * @author zhoujiagen
 * @see org.apache.hadoop.hdfs.MiniDFSCluster
 * @see org.apache.hadoop.mapred.MiniMRCluster
 */
public class TestMaxTemperatureJobDriverMiniCluster extends ClusterMapReduceTestCase {

  private static final Logger LOG =
      LoggerFactory.getLogger(TestMaxTemperatureJobDriverMiniCluster.class);

  @Test
  public void setUp() throws Exception {
    super.setUp();
  }

  @Test
  public void test() throws Exception {
    Configuration conf = super.createJobConf();
    Hadoops.output(conf);

    // 集群中输入和输出目录
    Path input = super.getInputDir();
    Path output = super.getOutputDir();
    // 1 创建HDFS输入目录, 拷贝数据到测试HDFS
    FileSystem fs = super.getFileSystem();
    Path localInput = new Path(ExampleConstants.DATA_NCDC_INPUT_PATH);
    fs.copyFromLocalFile(localInput, input);
    LOG.info("localInput={}, input={}, output={}", localInput, input, output);

    // 2 运行作业
    ExampleMaxTemperatureJobDriver driver = new ExampleMaxTemperatureJobDriver();
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
