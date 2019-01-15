package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.io.FileNotFoundException;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * <pre>
 * {@link FutureFallback}实例，已经废弃(会在20.0版本中移除)
 * 
 * 直接使用{@link AsyncFunction}
 * </pre>
 *
 * @author zhoujiagen
 * @see Futures#withFallback(ListenableFuture, FutureFallback)
 */
@SuppressWarnings("deprecation")
class ExampleFutureFallback implements FutureFallback<String> {

  @Override
  public ListenableFuture<String> create(Throwable t) throws Exception {
    Preconditions.checkNotNull(t);

    // 翻译异常
    if (t instanceof FileNotFoundException) {
      SettableFuture<String> future = SettableFuture.create();
      future.set("文件不存在");
      return future;
    }

    throw new Exception(t);
  }

}
