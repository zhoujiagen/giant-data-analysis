package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.spike.giantdataanalysis.hadoop.support.ApplicationConstants;
import com.spike.giantdataanalysis.hadoop.support.HadoopConstants_M2;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * Unit test of max temperature MapReduce job driver
 * @author zhoujiagen
 */
public class MaxTemperatureJobDriverTest {

  @BeforeClass
  public static void beforeClass() {
    Hadoops.SETUP_ENV();
  }

  // 进程中的作业运行器
  @Test
  public void testJobDriver() throws Exception {
    Configuration conf = new Configuration(false);
    conf.set(HadoopConstants_M2.Core.fs_default_name.key(), "file:///");
    conf.set(HadoopConstants_M2.MapReduce.mapreduce_framework_name.key(), "local");
    conf.setInt("mapreduce.task.io.sort.mb", 1);

    Path input = new Path(ApplicationConstants.DATA_NCDC_INPUT_PATH);
    Path output = new Path(ApplicationConstants.DATA_NCDC_OUTPUT_PATH);

    // delete old output path
    Hadoops.DELETE_LOCAL(conf, output, true);

    MaxTemperatureJobDriver jobDriver = new MaxTemperatureJobDriver();
    jobDriver.setConf(conf);

    int exitCode = jobDriver.run(new String[] { input.toString(), output.toString() });

    Assert.assertThat(exitCode, Is.is(0));
  }

}
