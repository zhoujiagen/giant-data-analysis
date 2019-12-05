package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.spike.giantdataanalysis.hadoop.example.ExampleConstants;
import com.spike.giantdataanalysis.hadoop.support.HadoopDefaultConstant;
import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * Unit test of max temperature MapReduce job driver
 * @author zhoujiagen
 * @see org.apache.hadoop.mapred.LocalJobRunner
 */
public class TestMaxTemperatureJobDriver {

  @BeforeClass
  public static void beforeClass() {
    Hadoops.setUpEnvironment();
  }

  // 进程中的作业运行器
  @Test
  public void testJobDriver() throws Exception {
    Configuration conf = new Configuration(true);
    conf.set(HadoopDefaultConstant.Core.fs_defaultFS.key(), "file:///");
    conf.set(HadoopDefaultConstant.MapReduce.mapreduce_framework_name.key(), "local");
    conf.setInt(HadoopDefaultConstant.MapReduce.mapreduce_task_io_sort_mb.key(), 1);

    Hadoops.output(conf);

    Path input = new Path(ExampleConstants.DATA_NCDC_INPUT_PATH);
    Path output = new Path(ExampleConstants.DATA_NCDC_OUTPUT_PATH);

    // delete old output path
    Hadoops.deleteLocalPath(conf, output, true);

    ExampleMaxTemperatureJobDriver jobDriver = new ExampleMaxTemperatureJobDriver();
    jobDriver.setConf(conf);

    int exitCode = jobDriver.run(new String[] { input.toString(), output.toString() });

    Assert.assertThat(exitCode, Is.is(0));
  }

}
