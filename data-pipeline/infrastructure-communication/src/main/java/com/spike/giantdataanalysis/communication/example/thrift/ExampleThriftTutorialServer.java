package com.spike.giantdataanalysis.communication.example.thrift;

import java.util.HashMap;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 * Apache Thrift示例.
 * <p>
 * REF <a href="https://thrift.apache.org/tutorial/java">Java Tutorial</a>
 * @author zhoujiagen@gmail.com
 */
public class ExampleThriftTutorialServer {

  public static void main(String[] args) {
    final int port = 8888;
    final boolean ssl = false;
    TutorialServer.start(port, ssl);
  }

  /**
   * 服务端应用
   * <p>
   * REF <a href="https://thrift.apache.org/docs/concepts">Thrift network stack</a>
   * 
   * <pre>
      +-------------------------------------------+
      | Server                                    |
      | (single-threaded, event-driven etc)       |
      +-------------------------------------------+
      | Processor                                 |
      | (compiler generated)                      |
      +-------------------------------------------+
      | Protocol                                  |
      | (JSON, compact etc)                       |
      +-------------------------------------------+
      | Transport                                 |
      | (raw TCP, HTTP etc)                       |
      +-------------------------------------------+
   * </pre>
   */
  static class TutorialServer {
    public static void start(final int port, boolean ssl) {
      try {
        final CalculatorHandler serviceHandler = new CalculatorHandler();
        // Processor
        final Calculator.Processor<CalculatorHandler> processor =
            new Calculator.Processor<CalculatorHandler>(serviceHandler);

        Runnable runnable = null;

        if (!ssl) {

          runnable = new Runnable() {
            @Override
            public void run() {
              try {
                // Transport
                TServerTransport serverTransport = new TServerSocket(port);
                // Server
                TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

                System.out.println("Start server without SSL...");
                server.serve();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          };

        } else {

          runnable = new Runnable() {

            @Override
            public void run() {
              try {
                // Transport
                TSSLTransportParameters parameters = new TSSLTransportParameters();
                String truststorepath = ".truststore";
                parameters.setTrustStore(truststorepath, "thrift", null, null);
                TServerTransport serverTransport =
                    TSSLTransportFactory.getServerSocket(port, 0, null, parameters);
                // Server
                TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

                System.out.println("Start server with SSL...");
                server.serve();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          };
        }

        Thread t = new Thread(runnable);
        t.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 服务实现
  static class CalculatorHandler implements Calculator.Iface {
    private HashMap<Integer, SharedStruct> log;

    public CalculatorHandler() {
      log = new HashMap<Integer, SharedStruct>();
    }

    public void ping() {
      System.out.println("ping()");
    }

    public int add(int n1, int n2) {
      System.out.println("add(" + n1 + "," + n2 + ")");
      return n1 + n2;
    }

    public int calculate(int logid, Work work) throws InvalidOperation {
      System.out.println(
        "calculate(" + logid + ", {" + work.op + "," + work.num1 + "," + work.num2 + "})");
      int val = 0;
      switch (work.op) {
      case ADD:
        val = work.num1 + work.num2;
        break;
      case SUBTRACT:
        val = work.num1 - work.num2;
        break;
      case MULTIPLY:
        val = work.num1 * work.num2;
        break;
      case DIVIDE:
        if (work.num2 == 0) {
          InvalidOperation io = new InvalidOperation();
          io.whatOp = work.op.getValue();
          io.why = "Cannot divide by 0";
          throw io;
        }
        val = work.num1 / work.num2;
        break;
      default:
        InvalidOperation io = new InvalidOperation();
        io.whatOp = work.op.getValue();
        io.why = "Unknown operation";
        throw io;
      }

      SharedStruct entry = new SharedStruct();
      entry.key = logid;
      entry.value = Integer.toString(val);
      log.put(logid, entry);

      return val;
    }

    public SharedStruct getStruct(int key) {
      System.out.println("getStruct(" + key + ")");
      return log.get(key);
    }

    public void zip() {
      System.out.println("zip()");
    }

  }
}
