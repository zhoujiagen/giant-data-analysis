package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import org.apache.log4j.spi.LoggingEvent;

public class DefaultLogFormatter implements LogFormatter {

  @Override
  public String format(LoggingEvent event) {
    return event.getMessage().toString();
  }
}
