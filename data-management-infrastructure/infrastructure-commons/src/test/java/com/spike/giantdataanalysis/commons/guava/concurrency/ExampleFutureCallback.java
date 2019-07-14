package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spike.giantdataanalysis.commons.lang.RandomUtils;

/**
 * <pre>
 * {@link FutureCallback}实例
 * </pre>
 *
 * @author zhoujiagen
 */
class ExampleFutureCallback {

  public static void main(String[] args) throws InterruptedException {
    ListeningExecutorService executor =
        MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    Callable<String> task = new Callable<String>() {
      @Override
      public String call() throws Exception {
        if (RandomUtils.nextBoolean()) {
          throw new Exception();
        } else {
          return "Yep!";
        }
      }
    };

    // 定义FutureCallback
    FutureCallback<String> futureCallback = new FutureCallback<String>() {
      @Override
      public void onSuccess(String result) {
        // 成功结果处理
        System.err.println("结果是: " + result);
      }

      @Override
      public void onFailure(Throwable t) {
        // 失败结果处理
        System.err.println("发生错误");
        t.printStackTrace();
      }
    };

    try {

      // 提交任务
      ListenableFuture<String> listenableFuture = executor.submit(task);

      // 添加回调，也可以在另一个Executor中执行
      Futures.addCallback(listenableFuture, futureCallback);

      // 多次尝试
      for (int i = 0; i < 10; i++) {
        listenableFuture = executor.submit(task);
        // 添加回调
        Futures.addCallback(listenableFuture, futureCallback);
      }

    } finally {

      Preconditions.checkNotNull(executor);
      executor.awaitTermination(10, TimeUnit.SECONDS);
    }

  }
}
