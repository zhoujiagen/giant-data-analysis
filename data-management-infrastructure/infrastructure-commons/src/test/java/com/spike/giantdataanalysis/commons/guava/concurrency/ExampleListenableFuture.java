package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spike.giantdataanalysis.commons.annotation.DesignPattern;
import com.spike.giantdataanalysis.commons.annotation.DesignPattern.Pattern;

/**
 * <pre>
 * {@link ListenableFuture}的实例
 * </pre>
 *
 * @author zhoujiagen
 */
class ExampleListenableFuture {

  public static void main(String[] args) throws IOException {

    Callable<Integer> callable = new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        Thread.sleep(3 * 1000L);
        return new Random(new Date().getTime()).nextInt();
      }
    };

    ExecutorService executorService = null;

    try {
      executorService = Executors.newCachedThreadPool();

      @DesignPattern(Pattern.Decorator)
      ListeningExecutorService listeningES = MoreExecutors.listeningDecorator(executorService);

      regularFutureExample(executorService, callable);
      listenableFutureExample(listeningES, callable);

    } finally {

      // 等待执行完成
      // System.in.read();

      Preconditions.checkNotNull(executorService);
      // executorService.shutdown();
      try {
        executorService.awaitTermination(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * <pre>
   * {@link ListenableFuture}的使用
   * </pre>
   * 
   * @param listeningES
   * @param callable
   */
  static void listenableFutureExample(ListeningExecutorService listeningES,
      Callable<Integer> callable) {
    final ListenableFuture<Integer> future = listeningES.submit(callable);

    Runnable listener = new Runnable() {
      @Override
      public void run() {
        try {
          System.err.println("现在Future执行完成了: " + future.get());
        } catch (InterruptedException | ExecutionException e) {
          // ignore
        }
      }
    };
    // 添加Listener，直接在同一个Executor中执行
    future.addListener(listener, listeningES);
  }

  /**
   * <pre>
   * 常规的{@link Future}使用
   * </pre>
   * 
   * @param executorService
   * @param callable
   */
  static void regularFutureExample(ExecutorService executorService, Callable<Integer> callable) {
    Preconditions.checkNotNull(executorService);
    Preconditions.checkNotNull(callable);

    Future<Integer> future = executorService.submit(callable);

    try {

      // block to get future's result
      Integer result = future.get();
      System.err.println(result);

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

  }

}
