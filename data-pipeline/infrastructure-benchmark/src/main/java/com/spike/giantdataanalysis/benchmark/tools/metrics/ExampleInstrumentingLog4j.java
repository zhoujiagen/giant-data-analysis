package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.log4j.InstrumentedAppender;

public class ExampleInstrumentingLog4j {
  private static Logger LOG = LoggerFactory.getLogger(ExampleInstrumentingLog4j.class);

  static {
    InstrumentedAppender appender =
        new InstrumentedAppender(ExampleMetricsHolder.I().getMetricRegistry());
    appender.activateOptions();
    LogManager.getLogger(ExampleInstrumentingLog4j.class).addAppender(appender);
    // LogManager.getRootLogger().addAppender(appender);
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    ExampleMetricsHolder.I().startConsoleReporter();

    Meter requestMeter = ExampleMetricsHolder.I().meter("requests");
    LOG.info(requestMeter.toString());

    for (int i = 0; i < 100; i++) {
      LOG.info(String.valueOf(i));
      Thread.sleep(100l);
      // 更新Meter
      requestMeter.mark(1l);
    }

    System.in.read();
  }

}
