package com.spike.giantdataanalysis.commons.io.nio2.async.socket;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * NIO2 features:<br/>
 * AsynchronousServerSocketChannel with Future, using customed executors
 * @see AsynchronousServerSocketChannel
 * @author zhoujiagen
 */
public class ExampleAsyncServerWithFutureUsingExecutors {

  public static void main(String[] args) {
    ExecutorService executorService =
        Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    try (AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open()) {
      if (!channel.isOpen()) {
        System.out.println("cannot open AsynchronousServerSocketChannel");
        return;
      }

      // set options
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

      // bind
      channel.bind(Sockets.DEFAULT_ADDRESS);

      System.out.println("wait for connections...");
      while (true) {
        Future<AsynchronousSocketChannel> future = channel.accept();

        try {
          final AsynchronousSocketChannel clientChannel = future.get();
          Callable<String> worker = new Callable<String>() {

            @Override
            public String call() throws Exception {
              String host = clientChannel.getRemoteAddress().toString();
              System.out.println("incoming connection from " + host);

              // echo
              ByteBuffer bb = ByteBuffer.allocateDirect(1024);
              while (clientChannel.read(bb).get() != -1) {
                bb.flip();
                clientChannel.write(bb).get();

                if (bb.hasRemaining()) {
                  bb.compact();
                } else {
                  bb.clear();
                }
              }
              System.out.println(
                "incoming connection from " + clientChannel.getRemoteAddress() + " served done");
              clientChannel.close();
              return host;
            }
          };

          executorService.submit(worker);
        } catch (InterruptedException | ExecutionException e) {
          System.out.println("closing server");
          executorService.shutdown();
          while (!executorService.isTerminated()) {
            System.out.print(".");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } // end of loop

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
