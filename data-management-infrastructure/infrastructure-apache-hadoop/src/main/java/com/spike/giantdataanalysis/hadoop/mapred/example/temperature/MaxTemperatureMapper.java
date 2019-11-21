package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

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
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String line = value.toString();

    String year = line.substring(15, 19);
    String temperature = line.substring(87, 92);

    if (!isMissingTemperature(temperature)) {

      int airTemperature = Integer.parseInt(temperature);
      context.write(new Text(year), new IntWritable(airTemperature));
    }

  }

  private boolean isMissingTemperature(String temperature) {
    return "+9999".equals(temperature);
  }

}
