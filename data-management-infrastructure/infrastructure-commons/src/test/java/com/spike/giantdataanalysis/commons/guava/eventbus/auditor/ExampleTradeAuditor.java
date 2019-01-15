package com.spike.giantdataanalysis.commons.guava.eventbus.auditor;

import java.util.List;

public interface ExampleTradeAuditor<E> {

  /**
   * <pre>
   * 审计，必须添加{@link @Subscribe}注解
   * </pre>
   * 
   * @param event 待审计事件
   * @see @Subscribe
   */
  public void audit(E event);

  /**
   * <pre>
   * 获取经审计的事件列表
   * </pre>
   * 
   * @return
   */
  public List<E> getEvents();

  /**
   * <pre>
   * 取消订阅
   * </pre>
   */
  public void unregister();

}
