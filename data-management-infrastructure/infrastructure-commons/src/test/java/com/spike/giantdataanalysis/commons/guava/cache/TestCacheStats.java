package com.spike.giantdataanalysis.commons.guava.cache;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * {@link CacheStats}的单元测试
 * 
 * 缓存的统计信息
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestCacheStats {
  private CacheLoader<String, ExampleCacheTradeAccount> cacheLoader =
      new CacheLoader<String, ExampleCacheTradeAccount>() {
        @Override
        public ExampleCacheTradeAccount load(String key) throws Exception {
          // 实际获取TradeAccount，这里mock实现
          return new ExampleCacheTradeAccount(key, "owner", 0.0d);
        }
      };

  @Test
  public void setAndGet() throws ExecutionException {

    LoadingCache<String, ExampleCacheTradeAccount> cache =
        CacheBuilder.newBuilder().recordStats().build(cacheLoader);

    cache.put("1", new ExampleCacheTradeAccount("1", "owner", 0.0d));

    ExampleCacheTradeAccount tradeAccount = cache.get("1");
    Assert.assertNotNull(tradeAccount);

    cache.refresh("1");

    // 不要获取的太早！！！
    CacheStats cacheStats = cache.stats();

    System.out.println(cacheStats.hitCount());
    System.out.println(cacheStats.loadSuccessCount());
    // 其他的统计信息

  }
}
