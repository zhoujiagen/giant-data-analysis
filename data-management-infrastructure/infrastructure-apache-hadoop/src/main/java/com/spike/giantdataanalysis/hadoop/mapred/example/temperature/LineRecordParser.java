package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import org.apache.hadoop.io.Text;

/**
 * Parser of line record
 * @author zhoujiagen
 */
public class LineRecordParser {
  private static final int MISSING_TEMPERATURE = 9999;

  private String year;
  private int airTemperature;
  private String quality;

  public void parse(Text record) {
    this.parse(record.toString());
  }

  public void parse(String record) {
    year = record.substring(15, 19);

    String airTemperatureString = record.substring(87, 92);
    airTemperature = Integer.parseInt(airTemperatureString);

    quality = record.substring(92, 93);
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
