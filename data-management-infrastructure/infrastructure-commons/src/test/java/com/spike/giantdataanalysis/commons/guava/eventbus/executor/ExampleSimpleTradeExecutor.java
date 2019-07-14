package com.spike.giantdataanalysis.commons.guava.eventbus.executor;

import java.util.Date;

import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeAccountEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeType;

/**
 * <pre>
 * 事件发布示例
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleSimpleTradeExecutor
    extends ExampleAbstractTradeExecutor<ExampleTradeAccountEvent> {

  public ExampleSimpleTradeExecutor(EventBus eventBus) {
    super(eventBus);
  }

  @Override
  public ExampleTradeAccountEvent constructEvent(Date tradeExecutionTime,
      ExampleCacheTradeAccount tradeAccount, double amount, ExampleTradeType tradeType) {
    return new ExampleTradeAccountEvent(amount, tradeExecutionTime, tradeType, tradeAccount);
  }

}
