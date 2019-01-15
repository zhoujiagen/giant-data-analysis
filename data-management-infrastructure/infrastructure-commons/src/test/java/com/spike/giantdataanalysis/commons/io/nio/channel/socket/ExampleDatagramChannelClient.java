package com.spike.giantdataanalysis.commons.io.nio.channel.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * a datagram time client,see RFC 868
 * @author zhoujiagen
 */
public class ExampleDatagramChannelClient {
  public static void main(String[] args) throws Exception {
    ExampleDatagramChannelClient client = new ExampleDatagramChannelClient(args);

    client.sendRequests();

    client.getReplies();
  }

  protected DatagramChannel channel;
  protected List<InetSocketAddress> remoteHosts;// multiple time servers
  protected int port;

  public ExampleDatagramChannelClient(String[] args) throws Exception {
    if (args.length == 0) {
      throw new Exception("Usage: [-p port] host ...");
    }
    parseArgs(args);
    this.channel = DatagramChannel.open();
  }

  /**
   * parse the input arguments
   * @param args
   */
  public void parseArgs(String[] args) {
    this.remoteHosts = new ArrayList<InetSocketAddress>();

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.equals("-p")) {
        i++;
        this.port = Integer.parseInt(args[i]);
        continue;
      }

      InetSocketAddress isa = new InetSocketAddress(arg, this.port);
      if (isa.getAddress() == null) {
        System.out.println("Cannot resolve address: " + arg);
        continue;
      }
      this.remoteHosts.add(isa);
    }
  }

  private void getReplies() throws Exception {
    ByteBuffer longBuffer = ByteBuffer.allocate(Long.SIZE / 8);
    longBuffer.order(ByteOrder.BIG_ENDIAN);
    longBuffer.putLong(0, 0);
    longBuffer.position(Integer.SIZE / 8);

    ByteBuffer bb = longBuffer.slice();

    int expectedReplies = this.remoteHosts.size();
    int replies = 0;

    System.out.println();
    System.out.println("Waiting for replies...");
    while (true) {
      InetSocketAddress isa = receivePacket(this.channel, bb);

      bb.flip();
      replies++;
      printTime(longBuffer.getLong(0), isa);

      if (replies == expectedReplies) {
        System.out.println("All packets answered");
        break;
      }

      System.out.println("Received " + replies + "/" + expectedReplies + " replies");
    }

  }

  private void printTime(long remote1900, InetSocketAddress isa) {
    long local = System.currentTimeMillis() / 1000;
    long remote = remote1900 - ExampleDatagramChannelServer.DIFF_1900;

    Date remoteDate = new Date(remote * 1000);
    Date localDate = new Date(local * 1000);

    long skew = remote - local;

    System.out.println("Reply from " + isa.getHostName() + ":" + isa.getPort());
    System.out.println(" there: " + remoteDate);
    System.out.println(" here: " + localDate);
    System.out.println(" skew(remote-local): " + skew);
  }

  private InetSocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer)
      throws Exception {
    buffer.clear();

    return (InetSocketAddress) channel.receive(buffer);
  }

  private void sendRequests() throws Exception {
    ByteBuffer bb = ByteBuffer.allocate(1);

    for (InetSocketAddress isa : this.remoteHosts) {
      System.out.println("Requesting time from " + isa.getHostName() + ":" + isa.getPort());

      bb.clear().flip();// see RFC868

      this.channel.send(bb, isa);
    }
  }
}
