package com.spike.giantdataanalysis.commons.messaging.disruptor;

import java.util.Date;
import java.util.concurrent.Callable;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.spike.giantdataanalysis.commons.messaging.disruptor.Disruptors.Configuration;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEvent;
import com.spike.giantdataanalysis.commons.messaging.disruptor.ExampleEvents.LongEventFactory;
import com.spike.giantdataanalysis.commons.messaging.disruptor.handler.DebugEventHandler;

/**
 * 存在依赖的消费者示例.
 * 
 * <pre>
 * REF: https://github.com/LMAX-Exchange/disruptor/wiki/Introduction
 * </pre>
 */
public class ExampleDependentConsumerApplication {
  public static void main(String[] args) {
    Configuration<LongEvent> configuration = new Configuration<>();
    configuration.setEventFactory(new LongEventFactory());
    configuration.setProducerType(ProducerType.SINGLE);
    Disruptor<LongEvent> disruptor = Disruptors.create(configuration);

    // the JournalConsumer and ReplicationConsumer can run freely in parallel with each other
    // The ApplicationConsumer depends on the JournalConsumer and ReplicationConsumer
    // try this case when RTFSC.
    EventHandler<LongEvent> journalConsummer = new DebugEventHandler<LongEvent>("JournalConsummer");
    EventHandler<LongEvent> replicationConsummer =
        new DebugEventHandler<LongEvent>("ReplicationConsummer");
    EventHandler<LongEvent> applicationConsummer =
        new DebugEventHandler<LongEvent>("ApplicationConsummer");
    disruptor.handleEventsWith(journalConsummer, replicationConsummer)//
        .then(applicationConsummer);

    disruptor.start();
    final ExampleLongEventProducer producer =
        new ExampleLongEventProducer(disruptor.getRingBuffer());
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
