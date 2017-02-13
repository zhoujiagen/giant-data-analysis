package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class DefaultLogFormatter implements LogFormatter {

  @Override
  public String format(ILoggingEvent event) {
    return event.getMessage().toString();
  }
}
