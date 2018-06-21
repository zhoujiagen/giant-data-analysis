package com.spike.giantdataanalysis.commons.messaging.disruptor.events;

/**
 * 可被清理的事件.
 */
public interface ClearableEvent {
  void clear();
}
