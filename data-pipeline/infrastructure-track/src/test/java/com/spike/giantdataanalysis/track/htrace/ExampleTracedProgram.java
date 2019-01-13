package com.spike.giantdataanalysis.track.htrace;

import java.util.Date;
import java.util.Random;

import org.apache.htrace.core.TraceScope;
import org.apache.htrace.core.Tracer;

/**
 * @author zhoujiagen@gmail.com
 */
public class ExampleTracedProgram {

  protected final Tracer tracer;

  protected static final Random rnd = new Random(new Date().getTime());

  public ExampleTracedProgram(Tracer tracer) {
    super();
    this.tracer = tracer;
  }

  public String simpleMethod(String input) {
    try (TraceScope scope = tracer.newScope("simple method")) {
      doSleep();
      return input;
    }
  }

  protected static void doSleep() {
    try {
      Thread.sleep(getNextSleepMs());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected static int getNextSleepMs() {
    long nextLong = rnd.nextLong();
    if (nextLong < 0) {
      nextLong = -nextLong;
    }
    return (int) (nextLong % 500);
  }

  public String methodWithCall(String input) {
    try (TraceScope scope = tracer.newScope("method with call")) {
      doSleep();
      return this.simpleMethod(input);
    }
  }

  public String methodWithMultiCall(String input, int count) {
    try (TraceScope scope = tracer.newScope("method with multi call")) {
      StringBuilder sb = new StringBuilder();
      if (count <= 0) {
        return sb.toString();
      }

      for (int i = 0; i < count; i++) {
        sb.append(this.simpleMethod(input));
      }
      return sb.toString();
    }
  }

  public String methodWithThread(final String input, final int count) {
    try (TraceScope scope = tracer.newScope("method with thread")) {

      final StringBuilder sb = new StringBuilder();
      if (count <= 0) {
        return sb.toString();
      }

      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          try (TraceScope scope = tracer.newScope("method with thread inner")) {
            for (int i = 0; i < count; i++) {
              sb.append(simpleMethod(input));
            }
          }
        }
      });

      thread.start();

      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      return sb.toString();
    }
  }

}
