package com.spike.giantdataanalysis.test.spike;

import java.util.Date;
import java.util.Random;

import com.google.common.util.concurrent.UncaughtExceptionHandlers;

public class TestThread {
  public static void main(String[] args) {
    final Random random = new Random(new Date().getTime());

    // 创建线程
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {

          long sum = 0l;
          for (int i = 1; i < 10000; i++) {
            sum += i;
          }
          if (sum > 0) {
          }

          try {
            Thread.sleep(500l);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
            System.err.println("thraed exit");
            break;
          }
        }
      }
    });
    t.start();

    // 主线程
    // 设置未捕获异常处理器
    Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit()); // Guava
    while (true) {
      System.out.println("state=" + t.getState());
      System.out.println("isAlive=" + t.isAlive());

      try {
        Thread.sleep(300l);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
        System.err.println("main thraed exit");
        break;
      }
    }

  }
}
