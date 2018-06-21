package com.spike.giantdataanalysis.commons.messaging.disruptor;

import java.util.concurrent.Callable;

import com.lmax.disruptor.RingBuffer;
import com.spike.giantdataanalysis.commons.messaging.core.MessageOperation;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEvent;

public class ExampleLongEventProducer implements MessageOperation<Long, Void> {

  private final RingBuffer<LongEvent> ringBuffer;

  public ExampleLongEventProducer(RingBuffer<LongEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void populateDate(Callable<Void> callable) {
    try {
      callable.call();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Void operate(Long value) {
    // Increment and return the next sequence for the ring buffer
    long sequence = ringBuffer.next();

    try {

      // Get the event for a given sequence in the RingBuffer.
      // 2 cases: publish, consume
      LongEvent e = ringBuffer.get(sequence);

      // Do some work with the event.
      e.set(value);

    } finally {

      // Publish the specified sequence.
      // This action marks this particular message as being available to be read.
      ringBuffer.publish(sequence);
    }
    return null;
  }

}
