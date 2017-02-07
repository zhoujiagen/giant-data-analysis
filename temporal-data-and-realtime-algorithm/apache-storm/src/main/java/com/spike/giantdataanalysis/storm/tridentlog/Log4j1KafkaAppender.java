package com.spike.giantdataanalysis.storm.tridentlog;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @see
 * @author zhoujiagen
 * @see FileAppender
 */
public class Log4j1KafkaAppender extends AppenderSkeleton {

  @Override
  public void close() {
    System.err.println("close...");
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }

  @Override
  protected void append(LoggingEvent event) {
    System.err.println(event);
  }
}
