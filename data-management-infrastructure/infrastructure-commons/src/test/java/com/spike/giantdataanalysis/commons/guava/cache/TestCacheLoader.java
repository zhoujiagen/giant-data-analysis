package com.spike.giantdataanalysis.commons.guava.cache;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheLoader;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * {@link CacheLoader}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 * @see Function
 * @see Supplier
 */
public class TestCacheLoader {

  @Test
  public void fromFunction() {
    Function<String, ExampleCacheTradeAccount> function =
        new Function<String, ExampleCacheTradeAccount>() {
          @Override
          public ExampleCacheTradeAccount apply(String input) {
            // 实际获取TradeAccount，这里mock实现
            return new ExampleCacheTradeAccount(input, "owner", 0.0d);
          }
        };

    CacheLoader<String, ExampleCacheTradeAccount> cacheLoader = CacheLoader.from(function);

    Assert.assertNotNull(cacheLoader);
  }

  @Test
  public void fromSupplier() {
    Supplier<ExampleCacheTradeAccount> supplier = new Supplier<ExampleCacheTradeAccount>() {
      @Override
      public ExampleCacheTradeAccount get() {
        // 直接生成
        return new ExampleCacheTradeAccount("<null>", "owner", 0.0d);
      }
    };

    CacheLoader<Object, ExampleCacheTradeAccount> cacheLoader = CacheLoader.from(supplier);

    Assert.assertNotNull(cacheLoader);
  }

}
