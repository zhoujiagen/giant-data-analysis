package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleSellEvent;

public class ExampleTradeSellAuditor extends ExampleAbstractTradeAuditor<ExampleSellEvent> {

  public ExampleTradeSellAuditor(EventBus eventBus) {
    super(eventBus);

    eventBus.register(this);
  }

  @Override
  protected void doAudit(ExampleSellEvent event) {
    System.out.println("Received sell event: " + event.toString());
  }

  @Override
  public void unregister() {
    eventbus.unregister(this);
  }
}
