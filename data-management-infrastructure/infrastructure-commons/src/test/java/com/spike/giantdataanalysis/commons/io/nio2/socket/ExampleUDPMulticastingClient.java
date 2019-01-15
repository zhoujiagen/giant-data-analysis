package com.spike.giantdataanalysis.commons.io.nio2.socket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * multicast client
 * @author zhoujiagen
 */
public class ExampleUDPMulticastingClient {
  public static void main(String[] args) {
    ByteBuffer bb = ByteBuffer.allocateDirect(Sockets.MAX_UDP_PACKET_SIZE);

    try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
      InetAddress groupAddress = InetAddress.getByName(Sockets.DEFAULT_MULTICAST_GROUPIP);
      if (!groupAddress.isMulticastAddress()) {
        System.out.println("not a valid multicast address");
        return;
      }

      if (!channel.isOpen()) {
        System.out.println("datagram channel cannot open");
        return;
      }

      // set options
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

      // bind
      channel.bind(new InetSocketAddress(Sockets.DEFAULT_PORT));

      // join the multicast group
      NetworkInterface ni = NetworkInterface.getByName(Sockets.DEFAULT_NETINTERFACE_NAME);
      MembershipKey key = channel.join(groupAddress, ni);

      // wait for data
      while (true) {
        if (key.isValid()) {
          channel.receive(bb);
          bb.flip();
          System.out.println(Sockets.DEFAULT_CHARSET.decode(bb));
          bb.clear();
        } else {
          break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
