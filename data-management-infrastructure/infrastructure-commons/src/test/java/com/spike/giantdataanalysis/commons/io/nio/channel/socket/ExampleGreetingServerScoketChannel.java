package com.spike.giantdataanalysis.commons.io.nio.channel.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.spike.giantdataanalysis.commons.io.util.Environments;

/**
 * Greeting Server
 * @author zhoujiagen
 * @see ServerSocketChannel
 */
public class ExampleGreetingServerScoketChannel {
  public static final String GREETING = "Hello I must be going" + Environments.NEWLINE;

  public static void main(String[] args) throws Exception {
    int port = 1234;// default port
    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
    }

    ByteBuffer bb = ByteBuffer.wrap(GREETING.getBytes());
    ServerSocketChannel ssc = ServerSocketChannel.open();

    ssc.socket().bind(new InetSocketAddress(port));
    ssc.configureBlocking(false);// non-blocking

    // infinite loop
    while (true) {
      System.out.println("Waiting for connections");

      SocketChannel sc = ssc.accept();
      if (sc == null) {
        Thread.sleep(2000L);
      } else {
        System.out.println("Incoming connection from " + sc.socket().getRemoteSocketAddress());

        bb.rewind();
        int responeByteCount = sc.write(bb);
        System.out.println("response " + responeByteCount + " bytes");

        Thread.sleep(5000L);// mock do other time consuming stuff

        sc.close();// server close client's connection
      }
    }

  }
}
