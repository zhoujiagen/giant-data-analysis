package com.spike.giantdataanalysis.commons.io.nio2.socket;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * A blocking echo TCP client
 * @author zhoujiagen
 */
public class ExampleBlockingTCPClient {
  public static void main(String[] args) {
    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
    ByteBuffer helloBuffer = ByteBuffer.wrap("Hello!".getBytes());

    // create socket channel
    try (SocketChannel channel = SocketChannel.open()) {
      // check socket channel state
      if (channel.isOpen()) {
        // set blocking mode
        channel.configureBlocking(true);

        // set options
        channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
        channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
        channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        channel.setOption(StandardSocketOptions.SO_LINGER, 5);

        // connect
        channel.connect(new InetSocketAddress(Sockets.DEFAULT_HOST, Sockets.DEFAULT_PORT));

        if (channel.isConnected()) {// check connection status
          // send data
          channel.write(helloBuffer);

          // read data
          while (channel.read(bb) != -1) {
            bb.flip();
            System.out.println(Sockets.DEFAULT_CHARSET.decode(bb));

            if (bb.hasRemaining()) {
              bb.compact();
            } else {
              bb.clear();
            }

            // send random number
            int r = new Random().nextInt(100);
            if (r % 5 == 0) {
              System.out.println("stop send data, caused by: " + r);
              break;
            } else {
              channel
                  .write(ByteBuffer.wrap("Random number: ".concat(String.valueOf(r)).getBytes()));
            }

          }

        } else {
          System.out.println("cannot establish connection");
        }

      } else {
        System.out.println("socket channel cannot open");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
