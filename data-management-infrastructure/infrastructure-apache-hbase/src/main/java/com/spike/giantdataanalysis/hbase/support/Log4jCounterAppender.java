package com.spike.giantdataanalysis.hbase.support;

import java.util.concurrent.atomic.AtomicLong;

public class Log4jCounterAppender extends org.apache.log4j.AppenderSkeleton {
  org.apache.log4j.Logger _LOG = org.apache.log4j.Logger.getLogger("org.apache.hadoop");

  public void init() {
    _LOG.removeAllAppenders();
    _LOG.setAdditivity(false);
    _LOG.addAppender(this);
    _LOG.setLevel(org.apache.log4j.Level.DEBUG);
  }

  private AtomicLong counter;

  public Log4jCounterAppender() {
    counter = new AtomicLong(0l);
  }

  @Override
  public void close() {
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }

  @Override
  protected void append(org.apache.log4j.spi.LoggingEvent event) {
    // String msg = event.getMessage().toString();
    // System.err.println(msg);

    counter.incrementAndGet(); // 计数
  }

  public long getCounter() {
    return counter.get();
  }

}