package com.spike.giantdataanalysis.commons.guava.eventbus.executor;

import java.util.Date;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeType;

public abstract class ExampleAbstractTradeExecutor<BaseTradeEvent>
    implements ExampleTradeExecutor<BaseTradeEvent> {

  protected EventBus eventBus;

  public ExampleAbstractTradeExecutor(EventBus eventBus) {
    Preconditions.checkNotNull(eventBus);

    this.eventBus = eventBus;
  }

  @Override
  public void execute(ExampleCacheTradeAccount tradeAccount, double amount,
      ExampleTradeType tradeType) {
    Date tradeExecutionTime = new Date();
    String message = String.format("Processed trade for %s of amount %n type %s @ %s", //
      tradeAccount.toString(), amount, tradeType, tradeExecutionTime);
    System.out.println(message);

    BaseTradeEvent event = this.constructEvent(tradeExecutionTime, tradeAccount, amount, tradeType);

    eventBus.post(event);
  }

  protected abstract BaseTradeEvent constructEvent(Date tradeExecutionTime,
      ExampleCacheTradeAccount tradeAccount, double amount, ExampleTradeType tradeType);

}
