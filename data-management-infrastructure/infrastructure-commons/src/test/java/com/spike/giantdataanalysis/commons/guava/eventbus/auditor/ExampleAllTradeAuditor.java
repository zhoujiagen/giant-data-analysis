package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleBaseTradeEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleBuyEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleSellEvent;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeType;

public class ExampleAllTradeAuditor extends ExampleAbstractTradeAuditor<ExampleBaseTradeEvent> {

  public ExampleAllTradeAuditor(EventBus eventBus) {
    super(eventBus);
  }

  @Override
  protected void doAudit(ExampleBaseTradeEvent event) {
    ExampleTradeType tradeType = null;

    if (event instanceof ExampleBuyEvent) {
      tradeType = ExampleTradeType.BUY;
    } else if (event instanceof ExampleSellEvent) {
      tradeType = ExampleTradeType.SELL;
    }

    System.out.println("Receive [" + tradeType + "] event");
  }

  /**
   * <pre>
   * 筛选出{@link ExampleTradeType#BUY}的事件
   * </pre>
   * 
   * @return
   */
  public List<ExampleBuyEvent> buyEvents() {
    List<ExampleBuyEvent> result = Lists.<ExampleBuyEvent> newArrayList();

    for (ExampleBaseTradeEvent event : events) {
      if (event instanceof ExampleBuyEvent) {
        result.add((ExampleBuyEvent) event);// cast
      }
    }

    return result;
  }

  @Override
  public void unregister() {
    eventbus.unregister(this);
  }
}
