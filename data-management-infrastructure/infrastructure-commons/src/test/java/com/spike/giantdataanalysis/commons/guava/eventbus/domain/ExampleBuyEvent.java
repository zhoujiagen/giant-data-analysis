package com.spike.giantdataanalysis.commons.guava.eventbus.domain;

import java.util.Date;

import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * 购买事件
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleBuyEvent extends ExampleTradeAccountEvent {

  public ExampleBuyEvent(double amount, Date tradeExecutionTime,
      ExampleCacheTradeAccount tradeAccount) {
    super(amount, tradeExecutionTime, ExampleTradeType.BUY, tradeAccount);
  }

}
