package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * <pre>
 * Mapper
 * 
 * 输入: (行在文件中的偏移量, 行内容)
 * 输出: (Text(year), Int(temperature))
 * </pre>
 * 
 * @author zhoujiagen
 */
public class ExampleMaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  enum MaxTemperatureCounterEnum {
    MISSING, OVER_100
  }

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String line = value.toString();

    String year = line.substring(15, 19);
    String temperature = line.substring(87, 92);

    if (!isMissingTemperature(temperature)) {
      int airTemperature = Integer.parseInt(temperature);
      if (airTemperature > 100) {
        // set current status of task
        context.setStatus("Detected possibly corrupt record: see logs.");
        // counter
        context.getCounter(MaxTemperatureCounterEnum.OVER_100).increment(1);
      } else {
        context.write(new Text(year), new IntWritable(airTemperature));
      }
    } else {
      context.getCounter(MaxTemperatureCounterEnum.MISSING).increment(1);
    }
  }

  private boolean isMissingTemperature(String temperature) {
    return "+9999".equals(temperature);
  }

}
