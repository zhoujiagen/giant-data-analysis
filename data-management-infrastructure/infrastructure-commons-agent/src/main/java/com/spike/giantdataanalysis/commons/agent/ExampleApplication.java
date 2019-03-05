package com.spike.giantdataanalysis.commons.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例应用.
 */
public class ExampleApplication {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleApplication.class);

  public void hello() {
    LOG.info("hello");
    try {
      Thread.sleep(5000L);
    } catch (InterruptedException e) {
      LOG.error("", e);
    }
  }
}
