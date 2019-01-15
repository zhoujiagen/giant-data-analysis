package com.spike.giantdataanalysis.commons.io.nio2.async.file;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

import com.spike.giantdataanalysis.commons.io.util.Environments;

public class ExampleAsyncFileReadWithFuture {
  public static void main(String[] args) {
    ByteBuffer bb = ByteBuffer.allocate(100);
    Path path = Paths.get(Environments.USER_HOME, "a.txt");

    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
      Future<Integer> future = channel.read(bb, 0);

      while (!future.isDone()) {
        System.out.print(".");
      }
      System.out.println();

      System.out.println("done? " + future.isDone());
      System.out.println("read " + future.get() + " bytes");

    } catch (Exception e) {
      e.printStackTrace();
    }

    bb.flip();
    System.out.println(Charset.forName(Environments.DEFAULT_FILE_ENCODING).decode(bb));
    bb.clear();
  }
}
