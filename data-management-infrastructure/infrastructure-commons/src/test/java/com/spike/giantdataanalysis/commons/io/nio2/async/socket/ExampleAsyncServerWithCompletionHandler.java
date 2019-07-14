package com.spike.giantdataanalysis.commons.io.nio2.async.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * NIO2 features:<br/>
 * AsynchronousServerSocketChannel with CompletionHandler
 * @see AsynchronousServerSocketChannel
 * @author zhoujiagen
 */
public class ExampleAsyncServerWithCompletionHandler {
  public static void main(String[] args) {
    try (AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open()) {
      if (!channel.isOpen()) {
        System.out.println("cannot open AsynchronousServerSocketChannel");
        return;
      }

      // set options
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

      // bind
      channel.bind(new InetSocketAddress(Sockets.DEFAULT_HOST, Sockets.DEFAULT_PORT));

      System.out.println("wait for connections...");
      channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
          channel.accept(null, this);

          try {
            System.out.println("incoming connection from " + result.getRemoteAddress());

            ByteBuffer bb = ByteBuffer.allocateDirect(1024);
            // echo
            while (result.read(bb).get() != -1) {
              bb.flip();
              System.out.println(bb);
              result.write(bb).get();

              if (bb.hasRemaining()) {
                bb.compact();
              } else {
                bb.clear();
              }
            }
            System.out
                .println("incoming connection from " + result.getRemoteAddress() + " served done");

          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            try {
              result.close();// close the client connection
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
          channel.accept(null, this);
          throw new RuntimeException("cannot accept connection");
        }
      });

      // wait, IMPORTANT
      System.in.read();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
