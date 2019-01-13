package com.spike.giantdataanalysis.track.htrace;

import org.apache.htrace.core.SpanId;
import org.apache.htrace.core.Tracer;

/**
 * @author zhoujiagen@gmail.com
 */
public class ExampleTracedRPCProgram extends ExampleTracedProgram {

  private MockingClient client;

  public ExampleTracedRPCProgram(Tracer tracer) {
    super(tracer);
    client = new MockingClient(tracer);
  }

  public void mocking(SpanId spanId) {
    client.echo(spanId, "hello");
  }

  public void shutdown() {
    client.shutdown();
  }

}
