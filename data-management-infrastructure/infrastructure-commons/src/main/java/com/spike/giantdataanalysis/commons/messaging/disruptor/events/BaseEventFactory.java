package com.spike.giantdataanalysis.commons.messaging.disruptor.events;

import com.lmax.disruptor.EventFactory;

public class BaseEventFactory<T> implements EventFactory<BaseEvent<T>> {

  @Override
  public BaseEvent<T> newInstance() {
    return new BaseEvent<T>();
  }

}
