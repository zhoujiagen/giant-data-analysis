package com.spike.giantdataanalysis.communication.thrift;

import java.util.HashMap;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import shared.SharedStruct;
import tutorial.Calculator;
import tutorial.Calculator.Client;
import tutorial.InvalidOperation;
import tutorial.Operation;
import tutorial.Work;

/**
 * 示例客户端和服务端应用.
 * 
 * <pre>
 * 相关文件: src/main/thrift/shared.thrift, tutorial.thrift
 * thrift编译生成的代码: src/main/thrift/generated-resources/shared, tutorial目录
 * </pre>
 * 
 * REF: http://thrift.apache.org/tutorial/java
 * @author zhoujiagen
 */
public final class ExampleTutorial {

  // 客户端应用
  static class TutorialClient {
    public static void start(String host, int port, boolean ssl) {
      try {

        TTransport transport = null;
        if (!ssl) {
          transport = new TSocket(host, port);
          transport.open();
        } else {
          TSSLTransportParameters parameters = new TSSLTransportParameters();
          String truststorepath = ".truststore";
          parameters.setTrustStore(truststorepath, "thrift", "SunX509", "JKS");
          transport = TSSLTransportFactory.getClientSocket(host, port, 0, parameters);
        }

        TProtocol protocol = new TBinaryProtocol(transport);
        Calculator.Client client = new Calculator.Client(protocol);

        play(client);

        transport.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    static void play(Client client) throws TException {
      // Calculator接口: void ping(),
      client.ping();
      System.out.println("ping()");

      // Calculator接口: i32 add(1:i32 num1, 2:i32 num2),
      int sum = client.add(1, 1);
      System.out.println("1+1=" + sum);

      // Calculator接口: i32 calculate(1:i32 logid, 2:Work w) throws (1:InvalidOperation ouch),
      Work work = new Work();
      work.op = Operation.DIVIDE;
      work.num1 = 1;
      work.num2 = 0;
      try {
        @SuppressWarnings("unused")
        int quotient = client.calculate(1, work);
        System.out.println("Whoa we can divide by 0");
      } catch (InvalidOperation io) {
        System.out.println("Invalid operation: " + io.why);
      }

      work.op = Operation.SUBTRACT;
      work.num1 = 15;
      work.num2 = 10;
      try {
        int diff = client.calculate(1, work);
        System.out.println("15-10=" + diff);
      } catch (InvalidOperation io) {
        System.out.println("Invalid operation: " + io.why);
      }

      // SharedService接口: SharedStruct getStruct(1: i32 key)
      SharedStruct log = client.getStruct(1);
      System.out.println("Check log: " + log.value);
    }
  }

  // 服务端应用
  static class TutorialServer {
    public static void start(final int port, boolean ssl) {
      try {
        final CalculatorHandler serviceHandler = new CalculatorHandler();
        final Calculator.Processor<CalculatorHandler> processor =
            new Calculator.Processor<CalculatorHandler>(serviceHandler);
        Thread t = null;
        if (!ssl) {
          t = new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                TServerTransport serverTransport = new TServerSocket(port);
                TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

                System.out.println("Start server without SSL...");
                server.serve();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });

        } else {

          t = new Thread(new Runnable() {

            @Override
            public void run() {
              try {
                TSSLTransportParameters parameters = new TSSLTransportParameters();
                String truststorepath = ".truststore";
                parameters.setTrustStore(truststorepath, "thrift", null, null);

                TServerTransport serverTransport =
                    TSSLTransportFactory.getServerSocket(port, 0, null, parameters);
                TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

                System.out.println("Start server with SSL...");
                server.serve();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });
        }

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
      System.out.println("calculate(" + logid + ", {" + work.op + "," + work.num1 + "," + work.num2
          + "})");
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

  public static void main(String[] args) throws InterruptedException {
    final int port = 8888;
    final boolean ssl = false;
    TutorialServer.start(port, ssl);

    // wait a few seconds
    Thread.sleep(2000l);

    TutorialClient.start("localhost", port, ssl);
  }
}
