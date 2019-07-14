package com.spike.giantdataanalysis.track.htrace;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.htrace.core.TraceScope;
import org.apache.htrace.core.Tracer;

class MockingWorker implements Callable<String> {

  final Tracer tracer;
  private final String input;
  private final Random rnd = new Random(new Date().getTime());

  public MockingWorker(Tracer tracer, String input) {
    super();
    this.tracer = tracer;
    this.input = input;
  }

  @Override
  public String call() throws Exception {
    try (TraceScope scope = tracer.newScope("worker call()")) {
      doSleep();
      return input;
    }
  }

  protected void doSleep() {
    try {
      Thread.sleep(this.getNextSleepMs());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected int getNextSleepMs() {
    long nextLong = rnd.nextLong();
    if (nextLong < 0) {
      nextLong = -nextLong;
    }
    return (int) (nextLong % 500);
  }
}