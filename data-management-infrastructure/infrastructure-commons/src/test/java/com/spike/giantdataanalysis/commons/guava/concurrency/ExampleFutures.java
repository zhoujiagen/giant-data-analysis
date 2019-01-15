package com.spike.giantdataanalysis.commons.guava.concurrency;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * <pre>
 * {@link Futures}的使用
 * </pre>
 *
 * @author zhoujiagen
 */
class ExampleFutures {

  public static void main(String[] args) {

    SettableFuture<String> future1 = SettableFuture.create();
    future1.set("future1");

    AsyncFunction<String, String> function = new AsyncFunction<String, String>() {
      @Override
      public ListenableFuture<String> apply(String input) throws Exception {
        Preconditions.checkNotNull(input);

        SettableFuture<String> result = SettableFuture.create();
        result.set(input.toUpperCase());

        return result;
      }
    };

    // 执行异步转换
    ListenableFuture<String> future = Futures.transformAsync(future1, function);

    // 添加回调
    Futures.addCallback(future, new FutureCallback<String>() {

      @Override
      public void onSuccess(String result) {
        System.err.println(result);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace();
      }
    });

  }

}
