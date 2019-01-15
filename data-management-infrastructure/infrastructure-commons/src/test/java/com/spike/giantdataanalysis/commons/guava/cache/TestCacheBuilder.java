package com.spike.giantdataanalysis.commons.guava.cache;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * {@link CacheBuilder}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 * @see CacheBuilder
 * @see CacheLoader
 */
public class TestCacheBuilder {

  private CacheLoader<String, ExampleCacheTradeAccount> cacheLoader;
  private RemovalListener<String, ExampleCacheTradeAccount> removalListener;

  @Before
  public void setUp() {
    cacheLoader = new CacheLoader<String, ExampleCacheTradeAccount>() {
      @Override
      public ExampleCacheTradeAccount load(String key) throws Exception {
        // 实际获取TradeAccount，这里mock实现
        return new ExampleCacheTradeAccount(key, "owner", 0.0d);
      }
    };

    removalListener = new ExampleCacheTradeAccountRemovalListener();
  }

  /**
   * <pre>
   *  演示{@link CacheBuilder}的配置项
   * </pre>
   */
  @Test
  public void create() {

    LoadingCache<String, ExampleCacheTradeAccount> tradeAccountCache = //
        CacheBuilder.newBuilder()//
            .expireAfterWrite(5L, TimeUnit.MINUTES)// 缓存写入后的过期时间
            .maximumSize(5000L)// 缓存的最大大小，使用LRU算法
            .removalListener(removalListener)// 删除的监听器
            .ticker(Ticker.systemTicker())// 时间源
            .build(cacheLoader)// 缓存加载器，键在而值不在时使用
    ;
    Assert.assertNotNull(tradeAccountCache);

    LoadingCache<String, ExampleCacheTradeAccount> tradeAccountCache2 = //
        CacheBuilder.newBuilder()//
            .expireAfterAccess(20L, TimeUnit.MINUTES)// 缓存访问后的过期时间
            .softValues()// 将值包装为SoftReference
            .removalListener(removalListener)//
            .build(cacheLoader)//
    ;
    Assert.assertNotNull(tradeAccountCache2);

    LoadingCache<String, ExampleCacheTradeAccount> tradeAccountCache3 = //
        CacheBuilder.newBuilder()//
            .concurrencyLevel(10)//
            .refreshAfterWrite(5L, TimeUnit.SECONDS)// 写入后自动刷新值
            .ticker(Ticker.systemTicker())//
            .build(cacheLoader)//
    ;
    Assert.assertNotNull(tradeAccountCache3);
  }

  /**
   * <pre>
   * 演示{@link CacheBuilderSpec}解析字符串，生成相应配置
   * </pre>
   */
  @Test
  public void spec() {
    // 方法1
    String literalSpec = "concurrencyLevel=10,expireAfterAccess=5m,softValues";
    LoadingCache<String, ExampleCacheTradeAccount> cache1 =
        CacheBuilder.from(literalSpec).build(cacheLoader);
    Assert.assertNotNull(cache1);

    // 方法2
    CacheBuilderSpec spec = CacheBuilderSpec.parse(literalSpec);
    LoadingCache<String, ExampleCacheTradeAccount> cache2 =
        CacheBuilder.from(spec).build(cacheLoader);
    Assert.assertNotNull(cache2);
  }
}
