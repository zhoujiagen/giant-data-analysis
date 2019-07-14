package com.spike.giantdataanalysis.commons.io.nio2.socket;

import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * a connected UDP client
 * @author zhoujiagen
 */
public class ExampleUDPConnectedClient {
  public static void main(String[] args) {

    ByteBuffer helloBuffer = ByteBuffer.wrap("Echo this: i am a simple server!".getBytes());
    ByteBuffer bb = ByteBuffer.allocateDirect(Sockets.MAX_UDP_PACKET_SIZE);

    try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
      if (!channel.isOpen()) {
        System.out.println("datagram channel cannot open");
        return;
      }

      // set options
      channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
      channel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

      // connect
      InetSocketAddress serverAddress =
          new InetSocketAddress(Sockets.DEFAULT_HOST, Sockets.DEFAULT_PORT);
      channel.connect(serverAddress);

      // send data

      // int sendBytes = channel.send(helloBuffer, serverAddress);
      int sendBytes = channel.write(helloBuffer);

      System.out.println("send " + sendBytes + " bytes to server");

      // receive
      // channel.receive(bb);
      channel.read(bb);

      bb.flip();
      System.out.println(Sockets.DEFAULT_CHARSET.decode(bb));
      bb.clear();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
