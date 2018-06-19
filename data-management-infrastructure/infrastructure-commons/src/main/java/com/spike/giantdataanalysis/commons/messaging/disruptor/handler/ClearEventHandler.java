package com.spike.giantdataanalysis.commons.messaging.disruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.spike.giantdataanalysis.commons.messaging.disruptor.events.ClearableEvent;

public class ClearEventHandler<T extends ClearableEvent> implements EventHandler<T> {

  private static final Logger LOG = LoggerFactory.getLogger(ClearEventHandler.class);

  @Override
  public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
    if (event instanceof ClearableEvent) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Clearing: Event={}, sequence={}, endOfBatch={}", event, sequence, endOfBatch);
      }
      ((ClearableEvent) event).clear();
    }
  }

}
