package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface LogFormatter {
  public static final String FIELD_LEVEL = "level";
  public static final String FIELD_LOGGER = "logger";
  public static final String FIELD_TIMESTAMP = "timestamp";
  public static final String FIELD_MESSAGE = "message";

  // for log4j
  //String format(LoggingEvent event);
  
  // for logback
  String format(ILoggingEvent event);
}
