package com.spike.giantdataanalysis.commons.messaging.disruptor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEvent;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEventFactory;
import com.spike.giantdataanalysis.commons.messaging.disruptor.handler.DebugEventHandler;

/**
 * 入门示例.
 * 
 * <pre>
 * REF: https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
 * </pre>
 */
public class ExampleEventApplication {

  public static void main(String[] args) {

    final EventFactory<LongEvent> eventFactory = new LongEventFactory();
    final int ringBufferSize = 1024;
    final ThreadFactory threadFactory =
        new ThreadFactoryBuilder().setThreadFactory(Executors.defaultThreadFactory()).build();
    final ProducerType producerType = ProducerType.MULTI;
    final WaitStrategy waitStrategy = // new BlockingWaitStrategy();
        // new SleepingWaitStrategy();
        // new YieldingWaitStrategy();
        new BusySpinWaitStrategy();

    // create the Disruptor
    Disruptor<LongEvent> disruptor =
        new Disruptor<>(eventFactory, ringBufferSize, threadFactory, producerType, waitStrategy);

    // connect the event handler, i.e. the consumer
    disruptor//
        .handleEventsWith(new ExampleLongEventHandler())//
        .then(new DebugEventHandler<>());

    // start the Disruptor
    RingBuffer<LongEvent> ringBuffer = disruptor.start();

    // publish events using the producer
    try {
      Thread.sleep(5000L);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    final ExampleLongEventProducer producer = new ExampleLongEventProducer(ringBuffer);
    producer.populateDate(new Callable<Void>() {

      @Override
      public Void call() throws Exception {
        long now = new Date().getTime();
        long end = now + 1000 * 60 * 2;
        for (long i = now; i < end; i++) {
          producer.operate(i);

          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    });

  }

}
