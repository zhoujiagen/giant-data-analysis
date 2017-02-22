package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import com.spike.giantdataanalysis.hadoop.support.HadoopConstants_M2;

/**
 * Unit test of max temperature MapReduce job driver
 * @author zhoujiagen
 */
public class MaxTemperatureJobDriverTest {

  @Test
  public void testJobDriver() throws Exception {
    Configuration conf = new Configuration(false);
    // conf.set(HadoopConstants.Common.fs_defaultFS_KEY, "file:///");
    conf.set(HadoopConstants_M2.Core.fs_default_name.key(), "file:///");
    // conf.set(HadoopConstants.MapReduce.mapreduce_framework_name_KEY, "local");
    conf.set(HadoopConstants_M2.MapReduce.mapreduce_framework_name.key(), "local");
    conf.setInt("mapreduce.task.io.sort.mb", 1);

    Path input = new Path("input");
    Path output = new Path("output");

    // delete old output path
    FileSystem fs = FileSystem.getLocal(conf);
    fs.delete(output, true);

    MaxTemperatureJobDriver jobDriver = new MaxTemperatureJobDriver();
    jobDriver.setConf(conf);

    int exitCode = jobDriver.run(new String[] { input.toString(), output.toString() });

    Assert.assertThat(exitCode, Is.is(0));
  }

}
