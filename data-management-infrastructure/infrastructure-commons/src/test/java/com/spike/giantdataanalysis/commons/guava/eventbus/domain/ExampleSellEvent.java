package com.spike.giantdataanalysis.commons.guava.eventbus.domain;

import java.util.Date;

import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * 卖出事件
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleSellEvent extends ExampleTradeAccountEvent {

  public ExampleSellEvent(double amount, Date tradeExecutionTime,
      ExampleCacheTradeAccount tradeAccount) {
    super(amount, tradeExecutionTime, ExampleTradeType.SELL, tradeAccount);
  }

}
