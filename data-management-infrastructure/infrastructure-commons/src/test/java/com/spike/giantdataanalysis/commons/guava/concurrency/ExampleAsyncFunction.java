package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.spike.giantdataanalysis.commons.lang.RandomUtils;

/**
 * <pre>
 * 异步函数{@link AsyncFunction}实例
 * </pre>
 *
 * @author zhoujiagen
 * @see SettableFuture
 */
class ExampleAsyncFunction implements AsyncFunction<Long, String> {

  public static void main(String[] args) throws Exception {
    ListeningExecutorService executor =
        MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    ExampleAsyncFunction function = new ExampleAsyncFunction(executor);

    FutureCallback<String> callback = new FutureCallback<String>() {
      @Override
      public void onSuccess(String result) {
        System.err.println(result);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace();
      }
    };

    try {
      Futures.addCallback(function.apply(1L), callback);

      // 第二次调用
      Futures.addCallback(function.apply(1L), callback);

    } finally {

      Preconditions.checkNotNull(executor);
      executor.awaitTermination(10, TimeUnit.SECONDS);
    }

  }

  /** 内部的缓存 */
  private ConcurrentMap<Long, String> innerMap = Maps.newConcurrentMap();

  /** 执行者 */
  private ListeningExecutorService executor;

  public ExampleAsyncFunction(ListeningExecutorService executor) {
    this.executor = executor;
  }

  /**
   * 类似于缓存，有则直接返回，没有则去获取
   */
  @Override
  public ListenableFuture<String> apply(final Long input) throws Exception {
    Preconditions.checkNotNull(input);

    if (innerMap.containsKey(input)) {

      SettableFuture<String> future = SettableFuture.create();
      future.set(innerMap.get(input));

      return future;

    } else {

      return executor.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
          // 模拟获取
          Thread.sleep(5 * 1000L);
          String value = String.valueOf(RandomUtils.nextBoolean());
          innerMap.put(input, value);// 缓存
          return String.valueOf(RandomUtils.nextBoolean());
        }
      });

    }
  }
}
