package com.spike.giantdataanalysis.communication.example.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Apache Thrift示例.
 * <p>
 * REF <a href="https://thrift.apache.org/tutorial/java">Java Tutorial</a>
 * @author zhoujiagen@gmail.com
 */
public class ExampleThriftTutorialClient {

  public static void main(String[] args) {
    final int port = 8888;
    final boolean ssl = false;
    TutorialClient.start("localhost", port, ssl);
  }

  // 客户端应用
  static class TutorialClient {
    public static void start(String host, int port, boolean ssl) {
      try {

        // Transport
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

        // Protocol
        TProtocol protocol = new TBinaryProtocol(transport);
        // Client
        Calculator.Client client = new Calculator.Client(protocol);

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

        transport.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
