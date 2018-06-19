package com.spike.giantdataanalysis.commons.messaging.disruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

/**
 * 调试事件处理器.
 * @param <E> event
 */
public class DebugEventHandler<E> implements EventHandler<E> {

  private String id = "DEBUG";

  public DebugEventHandler() {
  }

  public DebugEventHandler(String id) {
    this.id = id;
  }

  private static final Logger LOG = LoggerFactory.getLogger(DebugEventHandler.class);

  @Override
  public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
    LOG.info("{}: Event={}, sequence={}, endOfBatch={}", id, event, sequence, endOfBatch);
  }

}
