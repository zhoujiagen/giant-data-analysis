package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import com.google.common.eventbus.EventBus;
import com.spike.giantdataanalysis.commons.guava.eventbus.domain.ExampleTradeAccountEvent;

/**
 * <pre>
 * 事件订阅示例
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleSimpleTradeAuditor
    extends ExampleAbstractTradeAuditor<ExampleTradeAccountEvent> {

  public ExampleSimpleTradeAuditor(EventBus eventBus) {
    super(eventBus);
    // 注册
    eventBus.register(this);
  }

  @Override
  protected void doAudit(ExampleTradeAccountEvent event) {

    System.out.println("Received trade event: " + event.toString());
  }

  @Override
  public void unregister() {
    eventbus.unregister(this);
  }
}
