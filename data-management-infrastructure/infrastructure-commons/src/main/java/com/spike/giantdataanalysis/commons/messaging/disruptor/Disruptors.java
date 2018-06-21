package com.spike.giantdataanalysis.commons.messaging.disruptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor工具类.
 */
public final class Disruptors {

  private Disruptors() {
  }

  // ======================================== construction
  public static <T> Disruptor<T> create(Configuration<T> configuration) {
    Preconditions.checkArgument(configuration != null, "invalid configuration!");
    configuration.validate();

    return new Disruptor<>(configuration.eventFactory, configuration.ringBufferSize,
        configuration.threadFactory, configuration.producerType, configuration.waitStrategy);
  }

  // ======================================== chaining event handlers
  // more information see
  // [Disruptor Wizard](https://github.com/LMAX-Exchange/disruptor/wiki/Disruptor-Wizard)

  @SafeVarargs
  public static <T> EventHandlerGroup<T> parallel(Disruptor<T> disruptor,
      EventHandler<T>... eventHandlers) {
    Preconditions.checkArgument(eventHandlers.length > 0, "empty event handlers!");

    return disruptor.handleEventsWith(eventHandlers);
  }

  // firstEventHandler => secondEventHandler
  public static <T> EventHandlerGroup<T> follow(Disruptor<T> disruptor,
      EventHandler<T> firstEventHandler, EventHandler<T> secondEventHandler) {

    // return disruptor.after(firstEventHandler).handleEventsWith(secondEventHandler);
    return disruptor.handleEventsWith(firstEventHandler).then(secondEventHandler);
  }

  /**
   * Configuration of {@link Disruptor}.
   * @param <T> event type
   */
  public static class Configuration<T> {
    private EventFactory<T> eventFactory;
    private int ringBufferSize = 1024;
    private ThreadFactory threadFactory =
        new ThreadFactoryBuilder().setThreadFactory(Executors.defaultThreadFactory()).build();
    private ProducerType producerType = ProducerType.MULTI;
    private WaitStrategy waitStrategy = new BlockingWaitStrategy();

    public void validate() {
      Preconditions.checkArgument(eventFactory != null, "invalid event factor!");
      Preconditions.checkArgument(ringBufferSize > 0, "invalid ring buffer size!");
      Preconditions.checkArgument(threadFactory != null, "invalid thread factory");
      Preconditions.checkArgument(producerType != null, "invalid producer type");
      Preconditions.checkArgument(waitStrategy != null, "invalid wait stragety");
    }

    public EventFactory<T> getEventFactory() {
      return eventFactory;
    }

    public void setEventFactory(EventFactory<T> eventFactory) {
      this.eventFactory = eventFactory;
    }

    public int getRingBufferSize() {
      return ringBufferSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
      this.ringBufferSize = ringBufferSize;
    }

    public ThreadFactory getThreadFactory() {
      return threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
      this.threadFactory = threadFactory;
    }

    public ProducerType getProducerType() {
      return producerType;
    }

    public void setProducerType(ProducerType producerType) {
      this.producerType = producerType;
    }

    public WaitStrategy getWaitStrategy() {
      return waitStrategy;
    }

    public void setWaitStrategy(WaitStrategy waitStrategy) {
      this.waitStrategy = waitStrategy;
    }
  }

}
