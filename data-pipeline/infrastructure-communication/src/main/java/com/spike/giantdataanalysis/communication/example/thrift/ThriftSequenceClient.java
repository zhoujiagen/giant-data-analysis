package com.spike.giantdataanalysis.communication.example.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author zhoujiagen@gmail.com
 */
public class ThriftSequenceClient {

  public static void main(String[] args) throws TException {
    // Transport
    TTransport transport = new TSocket("localhost", ThriftSequenceServer.port);
    transport.open();

    // Protocol
    TProtocol protocol = new TBinaryProtocol(transport);
    // Client
    SequenceService.Client client = new SequenceService.Client(protocol);

    SequenceInitializeRequest request1 = new SequenceInitializeRequest("test", 0L);
    SequenceInitializeResponse response1 = client.initialize(request1);
    System.out.println(response1);

    SequenceCurrentRequest request2 = new SequenceCurrentRequest("test");
    SequenceCurrentResponse response2 = client.current(request2);
    System.out.println(response2);

    SequenceNextRequest request3 = new SequenceNextRequest("test");
    SequenceNextOneResponse response3 = client.nextOne(request3);
    System.out.println(response3);

    SequenceNextRequest request4 = new SequenceNextRequest("test");
    request4.setCount(2);
    SequenceNextNResponse response4 = client.nextN(request4);
    System.out.println(response4);

  }

}
