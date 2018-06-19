package com.spike.giantdataanalysis.commons.messaging.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEvent;

// the preferred approach
public class ExampleLongEventProducerWithTranslator {

  private final RingBuffer<LongEvent> ringBuffer;

  public ExampleLongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void onData(long value) {
    EventTranslatorOneArg<LongEvent, Long> eventTranslator =
        new EventTranslatorOneArg<LongEvent, Long>() {
          @Override
          public void translateTo(LongEvent event, long sequence, Long value) {
            event.set(value);
          }
        };

    ringBuffer.publishEvent(eventTranslator, value);
  }
}
