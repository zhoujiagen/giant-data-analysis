package com.spike.giantdataanalysis.commons.guava.eventbus.executor;

import java.util.Date;

import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleBaseTradeEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleBuyEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleSellEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeType;
import com.spike.giantdataanalysis.commons.lang.ExceptionUtils;

public class ExampleBuySellTradeExecutor
    extends ExampleAbstractTradeExecutor<ExampleBaseTradeEvent> {

  public ExampleBuySellTradeExecutor(EventBus eventBus) {
    super(eventBus);
  }

  @Override
  protected ExampleBaseTradeEvent constructEvent(Date tradeExecutionTime,
      ExampleCacheTradeAccount tradeAccount, double amount, ExampleTradeType tradeType) {
    if (ExampleTradeType.BUY.equals(tradeType)) {
      return new ExampleBuyEvent(amount, tradeExecutionTime, tradeAccount);
    } else if (ExampleTradeType.SELL.equals(tradeType)) {
      return new ExampleSellEvent(amount, tradeExecutionTime, tradeAccount);
    } else {
      throw ExceptionUtils.unsupport("不支持的交易类型！");
    }
  }

}
