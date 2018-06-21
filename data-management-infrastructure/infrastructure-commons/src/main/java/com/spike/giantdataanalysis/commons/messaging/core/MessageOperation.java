package com.spike.giantdataanalysis.commons.messaging.core;

/**
 * 消息操作.
 */
public interface MessageOperation<I, O> {
  O operate(I input);
}
