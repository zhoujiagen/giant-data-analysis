package com.spike.giantdataanalysis.commons.io.nio.channel.socket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * a datagram time server,see RFC 868
 * @author zhoujiagen
 * @see DatagramChannel
 */
public class ExampleDatagramChannelServer {
  public static final int DEFAULT_TIME_PORT = 37;
  public static final long DIFF_1900 = 2208988800L;

  protected DatagramChannel channel;

  public ExampleDatagramChannelServer(int port) throws Exception {
    this.channel = DatagramChannel.open();
    this.channel.socket().bind(new InetSocketAddress(port));
    System.out.println("Listening on port " + port + " for time requests");
  }

  public static void main(String[] args) throws Exception {
    int port = 12345;
    ExampleDatagramChannelServer server = new ExampleDatagramChannelServer(port);

    server.listen();
  }

  public void listen() throws Exception {
    ByteBuffer longBuffer = ByteBuffer.allocate(Long.SIZE / 8);
    longBuffer.order(ByteOrder.BIG_ENDIAN);
    longBuffer.putLong(0, 0);

    // lower 32 bit view
    longBuffer.position(Integer.SIZE / 8);
    ByteBuffer bb = longBuffer.slice();

    while (true) {
      bb.clear();

      SocketAddress sa = this.channel.receive(bb);
      if (sa == null) {
        continue;
      }

      System.out.println("Time request from " + sa);

      bb.clear();
      longBuffer.putLong(0, System.currentTimeMillis() / 1000 + DIFF_1900);
      this.channel.send(bb, sa);
    }
  }
}
