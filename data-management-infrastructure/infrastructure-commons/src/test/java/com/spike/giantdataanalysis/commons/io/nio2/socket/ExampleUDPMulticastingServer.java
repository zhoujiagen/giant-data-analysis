package com.spike.giantdataanalysis.commons.io.nio2.socket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * multicast server
 * @author zhoujiagen
 */
public class ExampleUDPMulticastingServer {

  // public static void main(String[] args) throws Exception {
  // // check multicast supported
  // SocketConstants.rendererNetworkInterface();
  // }

  public static void main(String[] args) {
    try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
      if (!channel.isOpen()) {
        System.out.println("datagram channel cannot open");
        return;
      }

      // set options
      NetworkInterface ni = NetworkInterface.getByName(Sockets.DEFAULT_NETINTERFACE_NAME);
      channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

      while (true) {
        Thread.sleep(2000L);

        System.out.println("sending data ...");
        InetSocketAddress groupAddress = new InetSocketAddress(
            InetAddress.getByName(Sockets.DEFAULT_MULTICAST_GROUPIP), Sockets.DEFAULT_PORT);
        channel.send(ByteBuffer.wrap(new Date().toString().getBytes()), groupAddress);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
