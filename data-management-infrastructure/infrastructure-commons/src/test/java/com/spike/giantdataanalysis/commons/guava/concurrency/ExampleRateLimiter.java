package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.RateLimiter;

/**
 * <pre>
 * {@link RateLimiter}实例
 * 
 * 可以作为限制方法被调用的次数约束来实现SLA
 * </pre>
 *
 * @author zhoujiagen
 */
class ExampleRateLimiter {
  public static void main(String[] args) {

    // 限制每秒允许被调用的次数
    double permitsPerSecond = 4.0;
    final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);

    final ExampleRateLimiter service = new ExampleRateLimiter();

    // 固定线程池大小
    ExecutorService executor = Executors.newFixedThreadPool(10);

    // 多次调用
    for (int i = 0; i < 10000; i++) {
      executor.submit(new Runnable() {
        @Override
        public void run() {
          service.usage2(rateLimiter);
        }
      });
    }

  }

  public void usage1(RateLimiter rateLimiter) {
    // 阻塞等待
    rateLimiter.acquire();

    System.out.println("OK!");
  }

  public void usage2(RateLimiter rateLimiter) {
    // 不阻塞
    if (rateLimiter.tryAcquire()) {
      System.err.println("OK!");

    } else {

      System.err.println("FAIL!");
      try {
        Thread.sleep(300L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
