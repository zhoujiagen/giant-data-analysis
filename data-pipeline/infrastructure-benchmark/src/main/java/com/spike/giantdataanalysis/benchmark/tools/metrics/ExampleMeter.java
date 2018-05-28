package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

/**
 * 简单的{@link Meter}示例.
 */
public class ExampleMeter {

  public static void main(String[] args) throws InterruptedException, IOException {
    MetricRegistry metricRegistry = ExampleMetricsHolder.I().getMetricRegistry();
    // A meter measures the rate of events over time (e.g., “requests per second”).
    Meter requestMeter = ExampleMetricsHolder.I().meter("requests");

    // 控制台输出报告器
    ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)//
        .convertRatesTo(TimeUnit.SECONDS)//
        .build();
    reporter.start(1, TimeUnit.SECONDS); // 按固定时间生成报告

    for (int i = 0; i < 100; i++) {
      Thread.sleep(200l);

      // 更新Meter
      requestMeter.mark(1l);
    }

    System.in.read();
  }
}
