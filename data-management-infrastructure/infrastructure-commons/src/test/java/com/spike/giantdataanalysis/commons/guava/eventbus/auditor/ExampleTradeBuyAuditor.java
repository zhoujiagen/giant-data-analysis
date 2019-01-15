package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleBuyEvent;

public class ExampleTradeBuyAuditor extends ExampleAbstractTradeAuditor<ExampleBuyEvent> {

  public ExampleTradeBuyAuditor(EventBus eventBus) {
    super(eventBus);

    eventBus.register(this);
  }

  @Override
  public void doAudit(ExampleBuyEvent event) {

    System.out.println("Received buy event: " + event.toString());
  }

  @Override
  public void unregister() {
    eventbus.unregister(this);
  }
}
