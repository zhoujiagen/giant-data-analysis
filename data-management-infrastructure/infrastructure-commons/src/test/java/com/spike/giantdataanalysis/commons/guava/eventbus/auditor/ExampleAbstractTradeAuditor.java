package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public abstract class ExampleAbstractTradeAuditor<E> implements ExampleTradeAuditor<E> {

  /** 事件列表 */
  protected List<E> events = new ArrayList<E>();

  /** 内部用{@link EventBus} */
  protected EventBus eventbus;

  public ExampleAbstractTradeAuditor(EventBus eventBus) {
    Preconditions.checkNotNull(eventBus);

    this.eventbus = eventBus;
  }

  /**
   * <pre>
   *  事件订阅方法，该方法只能有一个事件参数且只有一个参数
   * </pre>
   * 
   * @param event {@inheritDoc}
   * @see Subscribe
   */
  @Subscribe
  @Override
  public void audit(E event) {
    System.out.println("RECEIVER EVENT: " + event);
    events.add(event);

    doAudit(event);
  }

  /**
   * <pre>
   * 由子类实现的审计事件
   * </pre>
   * 
   * @param event
   */
  protected abstract void doAudit(E event);

  @Override
  public List<E> getEvents() {
    return events;
  }
}
