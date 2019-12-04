package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ExampleMaxTemperatureMapperUsingParser extends Mapper<LongWritable, Text, Text, IntWritable> {

  // introduce an line record parser
  private ExampleLineRecordParser parser = new ExampleLineRecordParser();

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    parser.parse(value);

    if (parser.isValidTemperature()) {
      context.write(new Text(parser.getYear()), new IntWritable(parser.getAirTemperature()));
    }

  }

}