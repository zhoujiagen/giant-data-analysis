package com.spike.giantdataanalysis.commons.messaging.disruptor.events;

/**
 * 基本的事件类.
 */
public class BaseEvent<T> {

  private T value;

  public void set(T value) {
    this.value = value;
  }

  public T get() {
    return value;
  }
}
