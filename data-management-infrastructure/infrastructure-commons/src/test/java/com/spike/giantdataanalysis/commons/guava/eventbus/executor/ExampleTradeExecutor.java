package com.spike.giantdataanalysis.commons.guava.eventbus.executor;

import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeType;

public interface ExampleTradeExecutor<BaseTradeEvent> {

  /**
   * <pre>
   * 执行交易
   * </pre>
   * 
   * @param tradeAccount 账户
   * @param amount 金额
   * @param tradeType 交易类型
   */
  public void execute(ExampleCacheTradeAccount tradeAccount, double amount,
      ExampleTradeType tradeType);
}
