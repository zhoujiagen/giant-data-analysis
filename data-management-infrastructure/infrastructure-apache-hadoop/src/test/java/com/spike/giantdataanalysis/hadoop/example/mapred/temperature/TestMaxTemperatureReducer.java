package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/**
 * {@link ExampleMaxTemperatureReducer}的MRUnit单元测试
 * @author zhoujiagen
 * @see ReduceDriver
 */
public class TestMaxTemperatureReducer {

  @Test
  public void returnsMaxIntegerInValues() throws IOException {

    Text key = new Text("1901");
    List<IntWritable> values = new ArrayList<IntWritable>();
    values.add(new IntWritable(5));
    values.add(new IntWritable(10));

    new ReduceDriver<Text, IntWritable, Text, IntWritable>()//
        .withReducer(new ExampleMaxTemperatureReducer())//
        .withInput(key, values)//
        .withOutput(key, new IntWritable(10))// 期望输出
        .runTest();
  }

}
