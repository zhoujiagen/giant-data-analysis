package com.spike.giantdataanalysis.commons.messaging.disruptor;

import com.lmax.disruptor.EventFactory;

public interface ExampleEvents {

  // ======================================== event

  class LongEvent {
    private long value;

    public void set(long value) {
      this.value = value;
    }

    public long get() {
      return value;
    }

    @Override
    public String toString() {
      return "LongEvent [value=" + value + "]";
    }

  }

  // ======================================== event factory
  class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
      return new LongEvent();
    }
  }

}
