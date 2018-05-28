package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.codahale.metrics.MetricRegistry;

public class ExampleHistogram {

  static class Request {
  }

  static class Response {
  }

  static class Service {
    void handlRequest(Request request, Response response) {
      long randomLong = new Random(new Date().getTime()).nextLong();
      if (randomLong < 0) randomLong = -randomLong;

      ExampleMetricsHolder.I().histogram(HISTOGRAM_NAME).update(randomLong);
    }
  }

  static final String HISTOGRAM_NAME =
      MetricRegistry.name(ExampleHistogram.class, "response-sizes");

  public static void main(String[] args) throws IOException, InterruptedException {
    ExampleMetricsHolder.I().startConsoleReporter();

    Service service = new Service();

    for (int i = 0; i < 100; i++) {
      service.handlRequest(new Request(), new Response());
      Thread.sleep(100l);
    }

    System.in.read();
  }

}
