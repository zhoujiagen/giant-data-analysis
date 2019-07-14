package com.spike.giantdataanalysis.commons.guava.cache;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * 自定义{@link RemovalListener}实现
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleCacheTradeAccountRemovalListener
    implements RemovalListener<String, ExampleCacheTradeAccount> {

  @Override
  public void onRemoval(RemovalNotification<String, ExampleCacheTradeAccount> notification) {

    // 键、值和原因
    System.err.println("REMOVAL: " + notification.getKey() + "=" + notification.getValue() //
        + ", caused by " + notification.getCause());
  }

}
