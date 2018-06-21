package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;

public class ExampleJmxReporter {

  public static void main(String[] args) throws IOException, InterruptedException {
    ExampleMetricsHolder.I().startConsoleReporter();

    JmxReporter jmxReporter = ExampleMetricsHolder.I().getJmxReporter();
    jmxReporter.start();

    Meter requestMeter = ExampleMetricsHolder.I().meter("requests");

    for (int i = 0; i < 100; i++) {
      Thread.sleep(1000l);
      // 更新Meter
      requestMeter.mark(1l);
    }

    System.in.read();
  }
}
