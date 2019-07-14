package com.spike.giantdataanalysis.commons.io.nio2.async.socket;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Random;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * NIO2 features:<br/>
 * AsynchronousSocketChannel with Future
 * @see AsynchronousSocketChannel
 * @author zhoujiagen
 */
public class ExampleAsyncClientWithFuture {
  public static void main(String[] args) {
    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
    ByteBuffer helloBuffer = ByteBuffer.wrap("Hi!".getBytes());

    try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
      if (!channel.isOpen()) {
        System.out.println("cannot open AsynchronousSocketChannel");
        return;
      }

      // set options
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
      channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

      // connect
      Void connect = channel.connect(Sockets.DEFAULT_ADDRESS).get();
      if (connect != null) {// null means success
        System.out.println("cannot establish connection");
        return;
      }

      System.out.println("local address: " + channel.getLocalAddress());

      channel.write(helloBuffer).get();

      while (channel.read(bb).get() != -1) {
        bb.flip();
        System.out.println(Sockets.DEFAULT_CHARSET.decode(bb));
        if (bb.hasRemaining()) {
          bb.compact();
        } else {
          bb.clear();
        }

        int r = new Random().nextInt(100);
        if (r % 5 == 0) {
          System.out.println("done caused by: " + r);
          break;
        } else {
          channel.write(ByteBuffer.wrap(("random number: ".concat(String.valueOf(r)).getBytes())))
              .get();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
