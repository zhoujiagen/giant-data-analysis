package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.util.Date;
import java.util.Random;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;

public class ExampleHealthCheck {

  static class DatabaseHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
      if (new Random(new Date().getTime()).nextBoolean()) {
        return Result.healthy("Database ok");
      } else {
        return Result.unhealthy(new IllegalStateException("Database error"));
      }
    }

  }

  static final String HEALTH_CHECK_NAME = MetricRegistry.name(ExampleHealthCheck.class, "mysql");

  public static void main(String[] args) throws InterruptedException {
    ExampleMetricsHolder.I().startConsoleReporter();

    HealthCheck databaseHealthCheck = new DatabaseHealthCheck();
    ExampleMetricsHolder.I().healthCheck(HEALTH_CHECK_NAME, databaseHealthCheck);

    // 执行检查
    for (int i = 0; i < 10; i++) {
      ExampleMetricsHolder.I().runHealthChecks(HEALTH_CHECK_NAME);
      Thread.sleep(1000l);
    }
  }
}
