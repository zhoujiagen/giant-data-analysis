package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapperUsingParser extends Mapper<LongWritable, Text, Text, IntWritable> {

  // introduce an line record parser
  private LineRecordParser parser = new LineRecordParser();

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    parser.parse(value);

    if (parser.isValidTemperature()) {
      context.write(new Text(parser.getYear()), new IntWritable(parser.getAirTemperature()));
    }

  }

}