package com.spike.giantdataanalysis.storm.tridentlog.formatter;

import org.apache.log4j.spi.LoggingEvent;

public interface LogFormatter {
  String format(LoggingEvent event);
}
