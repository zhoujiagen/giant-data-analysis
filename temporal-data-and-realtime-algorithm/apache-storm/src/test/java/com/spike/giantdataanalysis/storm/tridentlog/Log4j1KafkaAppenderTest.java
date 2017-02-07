package com.spike.giantdataanalysis.storm.tridentlog;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j1KafkaAppenderTest {

  private static final Logger LOG = LoggerFactory.getLogger(Log4j1KafkaAppenderTest.class);

  @Test
  public void testUsage() {
    LOG.info("hello");
  }
}
