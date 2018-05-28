package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.Map;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;

/**
 * REF M2_REPO/io/dropwizard/metrics/metrics-jvm/3.2.3/metrics-jvm-3.2.3.jar.
 */
public class ExampleJVM {

  public static void main(String[] args) throws InterruptedException, IOException {

    ExampleMetricsHolder.I().startConsoleReporter();

    JmxReporter jmxReporter = ExampleMetricsHolder.I().getJmxReporter();
    jmxReporter.start();

    MemoryUsageGaugeSet mugs = new MemoryUsageGaugeSet();
    Map<String, Metric> map = mugs.getMetrics();
    for (String name : map.keySet()) {
      ExampleMetricsHolder.I().register(name, map.get(name));
    }

    for (int i = 0; i < 100; i++) {
      Thread.sleep(1000l);
    }

    System.in.read();

  }
}
