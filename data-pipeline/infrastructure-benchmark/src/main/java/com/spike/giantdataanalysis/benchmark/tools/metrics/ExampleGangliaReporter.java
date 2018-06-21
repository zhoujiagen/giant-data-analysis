package com.spike.giantdataanalysis.benchmark.tools.metrics;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ganglia.GangliaReporter;

public class ExampleGangliaReporter {

  public static void main(String[] args) throws IOException, InterruptedException {
    final GMetric ganglia = new GMetric("118.178.253.28", 8649, UDPAddressingMode.UNICAST, 1);
    final GangliaReporter reporter =
        GangliaReporter.forRegistry(ExampleMetricsHolder.I().getMetricRegistry())//
            .convertRatesTo(TimeUnit.SECONDS)//
            .convertDurationsTo(TimeUnit.MILLISECONDS)//
            .build(ganglia);
    reporter.start(1, TimeUnit.MINUTES);

    Meter requestMeter = ExampleMetricsHolder.I()
        .meter(MetricRegistry.name(ExampleGangliaReporter.class, "requests"));

    for (int i = 0; i < 100000; i++) {
      Thread.sleep(1000l);
      // 更新Meter
      requestMeter.mark(1l);
    }

    System.in.read();

  }
}
