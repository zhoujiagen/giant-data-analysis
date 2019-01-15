package com.spike.giantdataanalysis.commons.guava.cache;

import java.util.concurrent.ExecutorService;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.util.concurrent.MoreExecutors;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * {@link RemovalListener}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestRemovalListener {

  @Test
  public void regular() {
    RemovalListener<String, ExampleCacheTradeAccount> removalListener =
        new ExampleCacheTradeAccountRemovalListener();

    Cache<String, ExampleCacheTradeAccount> cache =
        CacheBuilder.newBuilder().removalListener(removalListener).build();

    Assert.assertNotNull(cache);
  }

  @Test
  public void runInExecutors() {
    ExecutorService executorService = MoreExecutors.newDirectExecutorService();

    RemovalListener<String, ExampleCacheTradeAccount> removalListener =
        new ExampleCacheTradeAccountRemovalListener();

    RemovalListener<String, ExampleCacheTradeAccount> asyncRemovalListener = //
        RemovalListeners.asynchronous(removalListener, executorService);

    // 在其他线程池中执行
    Cache<String, ExampleCacheTradeAccount> cache =
        CacheBuilder.newBuilder().removalListener(asyncRemovalListener).build();

    Assert.assertNotNull(cache);
  }

}
