package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistry.MetricSupplier;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.HealthCheck.Result;

public class ExampleMetricsHolder {

  private static ExampleMetricsHolder INSTANCE = new ExampleMetricsHolder();

  private ExampleMetricsHolder() {
  }

  public static ExampleMetricsHolder I() {
    return INSTANCE;
  }

  private final MetricRegistry metricRegistry = new MetricRegistry();
  private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

  // 控制台输出报告器
  private final ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)//
      .convertRatesTo(TimeUnit.SECONDS)//
      .build();

  private JmxReporter jmxReporter;

  public JmxReporter getJmxReporter() {
    if (jmxReporter == null) {
      jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
    }
    return jmxReporter;
  }

  public ConsoleReporter getConsoleReporter() {
    return reporter;
  }

  public void startConsoleReporter() {
    reporter.start(1, TimeUnit.SECONDS); // 按固定时间生成报告
  }

  public MetricRegistry getMetricRegistry() {
    return metricRegistry;
  }

  public Meter meter(String name) {
    return metricRegistry.meter(name);
  }

  public <T extends Metric> T register(String name, T metric) {
    return metricRegistry.register(name, metric);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <T> Gauge<T> gauge(String name, final MetricSupplier<Gauge> supplier) {
    return metricRegistry.gauge(name, supplier);
  }

  public Counter counter(String name) {
    return metricRegistry.counter(name);
  }

  public Histogram histogram(String name) {
    return metricRegistry.histogram(name);
  }

  public Timer timer(String name) {
    return metricRegistry.timer(name);
  }

  public void healthCheck(String name, HealthCheck healthCheck) {
    healthCheckRegistry.register(name, healthCheck);
  }

  /**
   * 执行健康检查
   * @see HealthCheckRegistry#runHealthChecks()
   */
  public void runHealthChecks(String name) {
    Result result = healthCheckRegistry.runHealthCheck(name);
    if (result.isHealthy()) {
      System.out.println(result.getMessage());
    } else {
      System.out.println(result.getMessage());
      if (result.getError() != null) {
        result.getError().printStackTrace();
      }
    }

  }
}
