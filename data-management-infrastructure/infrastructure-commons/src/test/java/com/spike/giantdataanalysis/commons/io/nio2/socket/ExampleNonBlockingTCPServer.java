package com.spike.giantdataanalysis.commons.io.nio2.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.spike.giantdataanalysis.commons.io.util.Environments;
import com.spike.giantdataanalysis.commons.io.util.Sockets;

/**
 * a non-blocking tcp echo server
 * @author zhoujiagen Note:<br/>
 *         SocketChannel register both OP_READ and OP_WRITE interest operations,<br/>
 *         see {@link #handleReadableEvent(SelectionKey, Selector)}, and
 *         {@link #handleWritableEvent(SelectionKey, Selector)}
 */
public class ExampleNonBlockingTCPServer {
  private Map<SocketChannel, List<byte[]>> keepDataTrack =
      new HashMap<SocketChannel, List<byte[]>>();

  private ByteBuffer bb = ByteBuffer.allocate(2 * 1024);

  public static void main(String[] args) {
    ExampleNonBlockingTCPServer server = new ExampleNonBlockingTCPServer();
    server.start();
  }

  public void start() {
    // create selector and server socket channel
    try (Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open()) {

      // check state
      if (selector.isOpen() && channel.isOpen()) {
        // set non-blocking
        channel.configureBlocking(false);

        // set options
        channel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        // bind
        channel.bind(new InetSocketAddress(Sockets.DEFAULT_HOST, Sockets.DEFAULT_PORT));

        // register selection
        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("waiting for connections...");
        while (true) {
          // select incoming events
          selector.select();

          Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
          while (iter.hasNext()) {
            SelectionKey key = iter.next();

            // prevent the same key coming again
            iter.remove();

            if (!key.isValid()) {
              continue;
            }

            // dispatch event handler
            if (key.isAcceptable()) {
              this.handleAcceptableEvent(key, selector);
            } else if (key.isReadable()) {
              this.handleReadableEvent(key, selector);
            } else if (key.isWritable()) {
              this.handleWritableEvent(key, selector);
            }
          }

        } // end of loop
      } else {
        System.out.println("server socket channel or selector cannot open");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleAcceptableEvent(SelectionKey key, Selector selector) throws Exception {
    // should be ServerSocketChannel
    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
    SocketChannel clientChannel = serverChannel.accept();
    clientChannel.configureBlocking(false);
    System.out.println("incoming connection from: " + clientChannel.getRemoteAddress());

    clientChannel.write(ByteBuffer.wrap(("Hello!" + Environments.NEWLINE).getBytes()));

    keepDataTrack.put(clientChannel, new ArrayList<byte[]>());// ~

    // register socket channel OP_READ event
    clientChannel.register(selector, SelectionKey.OP_READ);
  }

  private void handleReadableEvent(SelectionKey key, Selector selector) {
    // should be SocketChannel
    SocketChannel clientChannel = (SocketChannel) key.channel();
    bb.clear();

    int bytes = -1;
    try {
      bytes = clientChannel.read(bb);

      if (bytes == -1) {// read completed
        keepDataTrack.remove(clientChannel);
        System.out.println("connection close by :" + clientChannel.getRemoteAddress());
        clientChannel.close();
        key.cancel();// ~
        return;
      }

      byte[] data = new byte[bytes];
      System.arraycopy(bb.array(), 0, data, 0, bytes);
      System.out.println(new String(data) + " from" + clientChannel.getRemoteAddress());

      this.echo(key, data);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void echo(SelectionKey key, byte[] data) {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    List<byte[]> channelHistoryDatas = keepDataTrack.get(clientChannel);
    channelHistoryDatas.add(data);// add data history

    key.interestOps(SelectionKey.OP_WRITE);// ~
  }

  private void handleWritableEvent(SelectionKey key, Selector selector) throws Exception {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    List<byte[]> channelHistoryDatas = keepDataTrack.get(clientChannel);
    Iterator<byte[]> iter = channelHistoryDatas.iterator();
    while (iter.hasNext()) {
      byte[] data = iter.next();
      iter.remove();
      clientChannel.write(ByteBuffer.wrap(data));
    }

    key.interestOps(SelectionKey.OP_READ);// ~
  }
}
