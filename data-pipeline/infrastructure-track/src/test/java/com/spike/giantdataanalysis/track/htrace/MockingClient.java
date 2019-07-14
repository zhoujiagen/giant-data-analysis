package com.spike.giantdataanalysis.track.htrace;

import java.util.concurrent.ExecutionException;

import org.apache.htrace.core.SpanId;
import org.apache.htrace.core.TraceScope;
import org.apache.htrace.core.Tracer;

/**
 * @author zhoujiagen@gmail.com
 */
class MockingClient {

  final Tracer tracer;
  private final MockingServer server;

  /**
   * @param server
   */
  public MockingClient(Tracer tracer) {
    super();
    this.tracer = tracer;
    this.server = new MockingServer(tracer);
  }

  public String echo(SpanId spanId, String input) {
    try (TraceScope scope = tracer.newScope("client echo RPC", spanId)) {
      String result1 = server.echo("ping1");
      String result2 = server.echo("ping2");
      String result3 = server.echo("ping3");
      return result1 + result2 + result3;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return null;
  }
  public void shutdown() {
    tracer.close();
    server.shutdown();
  }

}
