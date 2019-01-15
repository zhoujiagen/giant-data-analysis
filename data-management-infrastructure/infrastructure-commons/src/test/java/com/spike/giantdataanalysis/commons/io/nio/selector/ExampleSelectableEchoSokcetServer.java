package com.spike.giantdataanalysis.commons.io.nio.selector;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.spike.giantdataanalysis.commons.io.util.Environments;
import com.spike.giantdataanalysis.commons.io.util.NIOs;

/**
 * simple echo server<br/>
 * a single Selector is used to <br/>
 * (1)listen to the server socket and <br/>
 * (2)all the active socket channels
 * @author zhoujiagen
 */
public class ExampleSelectableEchoSokcetServer {
  protected static int PORT = 1234;

  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      PORT = Integer.parseInt(args[0]);
    } else {
      System.out.println("Usage: [port]");
    }

    new ExampleSelectableEchoSokcetServer().start(PORT);
  }

  /**
   * start the server
   * @param port
   * @throws Exception
   */
  public void start(int port) throws Exception {
    System.out.println("Listening on port " + port);

    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    ServerSocket serverSocket = serverSocketChannel.socket();

    Selector selector = Selector.open();

    serverSocket.bind(new InetSocketAddress(port));
    // registered channel must be in non-blocking mode
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);// register

    while (true) {
      // blocking, support a timeout version
      int readyCount = selector.select();

      System.out.println("readyCount = " + readyCount);
      if (readyCount == 0) {
        continue;
      }

      Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
      while (iter.hasNext()) {
        SelectionKey key = iter.next();
        System.out.println(key + "'s ready ops = " + key.readyOps());
        if (key.isAcceptable()) {// case 1: new connection

          // get channel through selection key
          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          SocketChannel socketChannel = server.accept();
          if (socketChannel != null) {
            System.out
                .println("new connection from " + socketChannel.socket().getRemoteSocketAddress()
                    + ":" + socketChannel.socket().getPort());
            this.registerChannel(selector, socketChannel, SelectionKey.OP_READ);

            this.sayHello(socketChannel);
          }
        }

        if (key.isReadable()) {
          // case 2: new data through connection
          SocketChannel socketChannel = (SocketChannel) key.channel();
          System.out
              .println("new incoming data from " + socketChannel.socket().getRemoteSocketAddress()
                  + ":" + socketChannel.socket().getPort());
          this.readDataFromSocket(key);
        }

        iter.remove();// remove this key
      }

      // Thread.sleep(5000L);
    }
  }

  private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

  protected void readDataFromSocket(SelectionKey key) throws Exception {
    SocketChannel socketChannel = (SocketChannel) key.channel();

    int count = 0;

    byteBuffer.clear();// should clear the byte buffer before read,
    // IMPORTANT!!!

    while ((count = socketChannel.read(byteBuffer)) > 0) {
      byteBuffer.flip();

      // FIXME read/write through same byte buffer
      while (byteBuffer.hasRemaining()) {
        System.out.println("response content: " + NIOs.detailsOfBuffer(byteBuffer));
        socketChannel.write(byteBuffer);// write it back
      }

      byteBuffer.clear();

    }

    if (count < 0) {
      // also invalidate the key
      socketChannel.close();
    }
  }

  private void sayHello(SocketChannel socketChannel) throws Exception {
    byteBuffer.clear();

    byteBuffer.put(("Hi there!" + Environments.NEWLINE).getBytes());

    byteBuffer.flip();

    socketChannel.write(byteBuffer);
  }

  private void registerChannel(Selector selector, SocketChannel socketChannel, int ops)
      throws Exception {
    if (socketChannel == null) {
      return;// gaurd
    }

    socketChannel.configureBlocking(false);
    socketChannel.register(selector, ops);
  }

}
