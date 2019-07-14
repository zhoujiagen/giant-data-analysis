package com.spike.giantdataanalysis.track.htrace;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.htrace.core.TraceScope;
import org.apache.htrace.core.Tracer;

class MockingServer {
  final Tracer tracer;
  final ExecutorService executors = Executors.newFixedThreadPool(5);

  public MockingServer(Tracer tracer) {
    super();
    this.tracer = tracer;
  }

  public String echo(String input) throws InterruptedException, ExecutionException {
    try (TraceScope scope = tracer.newScope("server echo()")) {
      Future<String> future = executors.submit(new MockingWorker(tracer, input));
      return future.get();
    }
  }

  public void shutdown() {
    executors.shutdown();
  }

}