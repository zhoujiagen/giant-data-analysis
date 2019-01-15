package com.spike.giantdataanalysis.commons.io.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * Constants used in Socket applications
 * @author zhoujiagen
 */
public class Sockets {
  public static final String DEFAULT_HOST = "127.0.0.1";

  public static final int DEFAULT_PORT = 1234;

  public static final InetSocketAddress DEFAULT_ADDRESS =
      new InetSocketAddress(Sockets.DEFAULT_HOST, Sockets.DEFAULT_PORT);

  public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

  public static final int MAX_UDP_PACKET_SIZE = 65507;

  public static final String DEFAULT_MULTICAST_GROUPIP = "225.4.5.6";
  /**
   * network interface name, <br/>
   * can be get from {@link #rendererNetworkInterface()}
   */
  public static final String DEFAULT_NETINTERFACE_NAME = "wlan0";

  public static void rendererNetworkInterface() throws Exception {
    Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();

    while (netInterfaces.hasMoreElements()) {
      NetworkInterface ni = netInterfaces.nextElement();
      System.out.println("Display Name: " + ni.getDisplayName());
      System.out.println("Up? " + ni.isUp());
      System.out.println("Support multicast?" + ni.supportsMulticast());
      System.out.println("Name: " + ni.getName());
      System.out.println("Virtual?" + ni.isVirtual());

      System.out.println("IP: ");
      Enumeration<InetAddress> ips = ni.getInetAddresses();
      while (ips.hasMoreElements()) {
        System.out.println(ips.nextElement());
      }

      System.out.println("-------------------------------------------");
    }

  }

}
