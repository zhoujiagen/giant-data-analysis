package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

public class ExampleGauge {

  static class QueueManager<T> {
    final Queue<T> queue;

    public QueueManager() {
      this.queue = new LinkedBlockingQueue<T>();

      MetricRegistry metricRegistry = ExampleMetricsHolder.I().getMetricRegistry();
      Metric gauge = new Gauge<Integer>() {
        @Override
        public Integer getValue() {
          return queue.size();
        }
      };
      // 注册Gauge
      metricRegistry.register(MetricRegistry.name(this.getClass(), "name", "size"), gauge);
    }

    public Queue<T> getQueue() {
      return queue;
    }

  }

  static class Job {
    private final String id;

    public Job(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException {

    ExampleMetricsHolder.I().startConsoleReporter();

    QueueManager<Job> qm = new QueueManager<>();

    for (int i = 0; i < 100; i++) {
      qm.getQueue().add(new Job(String.valueOf(i)));
      Thread.sleep(100l);
    }

    System.in.read();
  }

}
