package com.spike.giantdataanalysis.commons.io.nio2.async;

import java.io.IOException;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * NIO2 new features: asynchronous channel api<br/>
 * All IO operation have 2 forms:<br/>
 * <ul>
 * <li>Peding result: {@link Future}</li>
 * <li>Complete result: {@link CompletionHandler}</li>
 * </ul>
 * @author zhoujiagen
 * @see AsynchronousChannel
 * @see AsynchronousByteChannel
 * @see AsynchronousFileChannel
 * @see AsynchronousServerSocketChannel
 * @see AsynchronousSocketChannel
 * @see AsynchronousChannelGroup
 */
public class TestAsyncEntry {

  /**
   * @see AsyncFileReadWithFuture
   */
  @Test
  public void asyncFileReadWithFuture() {
  }

  /**
   * @see AsyncFileWriteWithFuture
   */
  @Test
  public void asyncFileWriteWithFuture() {
  }

  /**
   * @see AsyncFileReadWithFutureUsingTimeout
   */
  @Test
  public void asyncFileReadWithFutureUsingTimeout() {
  }

  /**
   * @see AsyncFileReadWithCompletionHandler
   */
  @Test
  public void asyncFileReadWithCompletionHandler() {
  }

  /**
   * @see AsyncFileLock
   */
  @Test
  public void asyncFileLock() {
  }

  /**
   * @see AsyncFileChannelWithExecutorServer
   */
  @Test
  public void asyncFileChannelWithExecutorServer() {
  }

  // --------------------------------------------------------------------------------------------------------------------------------

  /**
   * @see AsyncServerWithFuture
   * @see AsyncServerWithFutureUsingExecutors
   * @see AsyncClientWithFuture
   */
  @Test
  public void asyncSocketWithFuture() {
  }

  /**
   * @see AsyncServerWithCompletionHandler
   * @see AsyncClientWithCompletionHandler
   */
  @Test
  public void asyncSocketWithCompletionHandler() {
  }

  /**
   * @see AsynchronousSocketChannel#read(java.nio.ByteBuffer, Object, CompletionHandler)
   * @see AsynchronousSocketChannel#write(java.nio.ByteBuffer, Object, CompletionHandler)
   */
  @Test
  public void readWriteUsingCompletionHandler() {
  }

  @Test
  public void asyncCustomGroup() {
    AsynchronousServerSocketChannel channel = null;
    try {
      AsynchronousChannelGroup group =
          AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(5));
      channel = AsynchronousServerSocketChannel.open(group);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        channel.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}
