package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import org.apache.log4j.spi.LoggingEvent;

public interface LogFormatter {
  public static final String FIELD_LEVEL = "level";
  public static final String FIELD_LOGGER = "logger";
  public static final String FIELD_TIMESTAMP = "timestamp";
  public static final String FIELD_MESSAGE = "message";

  String format(LoggingEvent event);
}
