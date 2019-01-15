package com.spike.giantdataanalysis.commons.io.nio.channel.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.spike.giantdataanalysis.commons.io.util.NIOs;

/**
 * Greeting Client
 * @author zhoujiagen
 * @see SocketChannel
 */
public class ExampleGreetingSocketChannel {

  public static void main(String[] args) throws Exception {
    // default server address and port
    String host = "localhost";
    int port = 1234;
    if (args.length == 2) {
      host = args[0];
      port = Integer.parseInt(args[1]);
    }
    InetSocketAddress address = new InetSocketAddress(host, port);
    SocketChannel sc = SocketChannel.open();
    sc.configureBlocking(false);// non-blocking

    System.out.println("initiating connection");
    sc.connect(address);

    // check connection state
    while (!sc.finishConnect()) {
      System.out.print(".");
    }
    System.out.println();

    System.out.println("connection established");

    ByteBuffer bb = ByteBuffer.allocate(1024);

    // do something with socket channel, mock bloking read
    int readByteCount = 0;
    while (readByteCount < ExampleGreetingServerScoketChannel.GREETING.length()) {
      readByteCount += sc.read(bb);
    }
    System.out.println("read server respones: " + readByteCount + " bytes");
    bb.flip();
    System.out.println(NIOs.detailsOfBuffer(bb));
    StringBuffer sb = new StringBuffer();
    while (bb.hasRemaining()) {
      sb.append((char) bb.get());
    }
    System.out.println(sb.toString());
    Thread.sleep(5000L);

    sc.close();
  }
}
