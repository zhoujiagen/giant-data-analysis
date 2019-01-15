package com.spike.giantdataanalysis.commons.io.nio2.async.file;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

import com.spike.giantdataanalysis.commons.io.util.Environments;

public class ExampleAsyncFileLock {
  public static void main(String[] args) {
    // usingFuture();

    usingCompletionHander();
  }

  static void usingFuture() {
    String content = "what is rational is real and what is real is rational.";
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 5; i++) {
      sb.append(content).append(Environments.NEWLINE);
    }

    ByteBuffer bb = ByteBuffer.wrap(sb.toString().getBytes());
    Path path = Paths.get(Environments.USER_HOME, "a.txt");

    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
      Future<FileLock> lockFuture = channel.lock();
      FileLock fileLock = lockFuture.get();

      if (fileLock.isValid()) {
        Future<Integer> writeFuture = channel.write(bb, 0);
        int bytes = writeFuture.get();

        System.out.println("write " + bytes + " bytes");

        fileLock.release();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Thread currentThread;

  static void usingCompletionHander() {
    Path path = Paths.get(Environments.USER_HOME, "a.txt");

    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
      currentThread = Thread.currentThread();

      String attachment = "lock operation status";
      channel.lock(attachment, new MyCompletionHandler(channel));

      // wait for read operation done
      try {
        currentThread.join();
      } catch (InterruptedException ignore) {
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  static class MyCompletionHandler implements CompletionHandler<FileLock, Object> {
    Path path;
    AsynchronousFileChannel channel;

    MyCompletionHandler(Path path) {
      this.path = path;
    }

    MyCompletionHandler() {
    }

    public MyCompletionHandler(AsynchronousFileChannel channel) {
      this.channel = channel;
    }

    @Override
    public void completed(FileLock result, Object attachment) {
      System.out.println(attachment + " " + result.isValid());

      if (result.isValid()) {
        System.out.println("processing the locked file");
        try {
          ByteBuffer bb = ByteBuffer.allocate(100);
          channel.read(bb, 0).get();
          bb.flip();
          System.out.println(Charset.forName(Environments.DEFAULT_FILE_ENCODING).decode(bb));
          bb.clear();

          // release lock
          result.release();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      currentThread.interrupt();
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
      System.out.println(attachment);
      System.out.println("failed: " + exc);

      currentThread.interrupt();
    }
  }
}
