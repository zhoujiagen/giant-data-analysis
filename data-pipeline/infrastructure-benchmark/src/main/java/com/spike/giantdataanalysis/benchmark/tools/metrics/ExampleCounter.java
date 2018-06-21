package com.spike.giantdataanalysis.benchmark.tools.metrics;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.codahale.metrics.MetricRegistry;

public class ExampleCounter {

  static class QueueManager<T> {
    final Queue<T> queue;

    public QueueManager() {
      this.queue = new LinkedBlockingQueue<T>();
    }

    public Queue<T> getQueue() {
      return queue;
    }

    public void add(T e) {
      // 增加计数器
      ExampleMetricsHolder.I().counter(COUNTER_NAME).inc();
      queue.add(e);
    }

    public T take() {
      T e = queue.poll();
      if (e != null) {
        // 减少计数器
        ExampleMetricsHolder.I().counter(COUNTER_NAME).dec();
      }
      return e;
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

  static final String COUNTER_NAME = MetricRegistry.name(ExampleCounter.class, "pending-jobs");

  public static void main(String[] args) throws InterruptedException, IOException {
    ExampleMetricsHolder.I().startConsoleReporter();

    QueueManager<Job> jobqm = new QueueManager<>();
    for (int i = 0; i < 100; i++) {
      jobqm.add(new Job("1"));
      Thread.sleep(100l);
      if (i % 3 == 0 || i % 5 == 0) {
        jobqm.take();
      }
    }

    System.in.read();
  }

}
