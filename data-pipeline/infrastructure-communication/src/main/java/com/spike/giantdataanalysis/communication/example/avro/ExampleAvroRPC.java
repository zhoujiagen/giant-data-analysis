package com.spike.giantdataanalysis.communication.example.avro;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Avro RPC实例.
 * <p>
 * REF <a href="https://github.com/phunt/avro-rpc-quickstart">Apache Avro RPC Quick Start.</a>
 * @author zhoujiagen@gmail.com
 */
public class ExampleAvroRPC {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleAvroRPC.class);

  public static class MailImpl implements Mail {
    public Utf8 send(Message message) {
      System.out.println("Sending message");
      return new Utf8("Sending message to " + message.getTo().toString() + " from "
          + message.getFrom().toString() + " with body " + message.getBody().toString());
    }
  }

  private static Server server;

  private static void startServer() throws IOException {
    server = new NettyServer(new SpecificResponder(Mail.class, new MailImpl()),
        new InetSocketAddress(65111));
  }

  public static void main(String[] args) throws IOException {
    // 启动服务器
    LOG.info("Starting server");
    startServer();
    LOG.info("Server started");

    // 使用客户端
    NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
    Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
    LOG.info("Client built, got proxy");

    Message message = new Message();
    message.setTo(new Utf8("to mail"));
    message.setFrom(new Utf8("from mail"));
    message.setBody(new Utf8("main body"));
    LOG.info("Calling proxy.send with message:  " + message.toString());
    LOG.info("Result: " + proxy.send(message));

    client.close();
    server.close();
  }
}
