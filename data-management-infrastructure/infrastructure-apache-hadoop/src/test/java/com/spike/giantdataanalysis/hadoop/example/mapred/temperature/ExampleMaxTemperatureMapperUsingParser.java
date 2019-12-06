package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ExampleMaxTemperatureMapperUsingParser
    extends Mapper<LongWritable, Text, Text, IntWritable> {

  /**
   * Parser of line record
   */
  public static class ExampleLineRecordParser {
    private static final int MISSING_TEMPERATURE = 9999;

    private String year;
    private int airTemperature;
    private String quality;
    private String USAFStationId;

    public void parse(Text record) {
      this.parse(record.toString());
    }

    public void parse(String record) {
      USAFStationId = record.substring(4, 10);
      year = record.substring(15, 19);

      String airTemperatureString = record.substring(87, 92);
      airTemperature = Integer.parseInt(airTemperatureString);

      quality = record.substring(92, 93);
    }

    public String getUSAFStationId() {
      return USAFStationId;
    }

    public boolean isValidTemperature() {
      return airTemperature != MISSING_TEMPERATURE && quality.matches("[01459]");
    }

    public String getYear() {
      return year;
    }

    public int getAirTemperature() {
      return airTemperature;
    }

  }

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