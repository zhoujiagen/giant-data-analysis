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
