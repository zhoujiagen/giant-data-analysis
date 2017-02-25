package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.ClusterMapReduceTestCase;
import org.junit.Test;

import com.spike.giantdataanalysis.hadoop.support.ApplicationConstants;

/**
 * REF tomwhite/hadoop-book: https://github.com/tomwhite/hadoop-book
 * @author zhoujiagen
 */
public class MaxTemperatureJobDriverMiniClusterTest extends ClusterMapReduceTestCase {

  @Test
  public void setUp() throws Exception {
    super.setUp();
  }

  @Test
  public void test() throws IOException {
    Configuration conf = super.createJobConf();

    Path localInput = new Path(ApplicationConstants.DATA_NCDC_INPUT_PATH);
    Path input = super.getInputDir();
    @SuppressWarnings("unused")
    Path output = super.getOutputDir();

    super.getFileSystem().copyFromLocalFile(localInput, input);

    MaxTemperatureJobDriver driver = new MaxTemperatureJobDriver();
    driver.setConf(conf);

  }
}
