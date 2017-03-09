package com.spike.giantdataanalysis.storm.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

  public static final Logger LOG = LoggerFactory.getLogger(LogTest.class);

  @Test
  public void log() {
    for (int i = 0; i < 10; i++) {
      LOG.info(String.valueOf(i));
    }
  }

}
