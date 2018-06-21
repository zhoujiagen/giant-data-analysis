package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class ExampleTimer {
  static class Request {
  }

  static class Response {
  }

  static class Service {
    void handlRequest(Request request, Response response) {
      Timer timer = ExampleMetricsHolder.I().timer(TIMERNAME);
      Timer.Context context = timer.time();

      long randomLong = new Random(new Date().getTime()).nextLong();
      if (randomLong < 0) randomLong = -randomLong;

      // mock process time
      try {
        Thread.sleep(randomLong % 200);
      } catch (InterruptedException e) {// just ignore
      }

      context.stop();
    }
  }

  static final String TIMERNAME = MetricRegistry.name(ExampleHistogram.class, "responses");

  public static void main(String[] args) throws IOException {
    ExampleMetricsHolder.I().startConsoleReporter();

    Service service = new Service();
    for (int i = 0; i < 100; i++) {
      service.handlRequest(new Request(), new Response());
    }

    System.in.read();
  }
}
