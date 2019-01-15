package com.spike.giantdataanalysis.commons.guava.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * <pre>
 * 订阅取消订阅事件
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleDeadEventSubscriber {
  public ExampleDeadEventSubscriber(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void handleUnsubsribeEvent(DeadEvent deadEvent) {
    System.out.println("No subscribers for " + deadEvent.getEvent());
  }

}
