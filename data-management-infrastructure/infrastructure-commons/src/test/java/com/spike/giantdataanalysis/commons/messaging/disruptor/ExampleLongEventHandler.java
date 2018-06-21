package com.spike.giantdataanalysis.commons.messaging.disruptor;

import com.lmax.disruptor.EventHandler;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEvent;

public class ExampleLongEventHandler implements EventHandler<LongEvent> {

  @Override
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("event=" + event + ", sequence=" + sequence + ", endOfBatch=" + endOfBatch);
  }

}
