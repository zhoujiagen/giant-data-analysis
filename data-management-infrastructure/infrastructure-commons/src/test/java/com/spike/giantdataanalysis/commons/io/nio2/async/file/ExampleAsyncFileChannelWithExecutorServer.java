package com.spike.giantdataanalysis.commons.io.nio2.async.file;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import com.spike.giantdataanalysis.commons.io.util.Environments;

public class ExampleAsyncFileChannelWithExecutorServer {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    List<Future<ByteBuffer>> futures = new ArrayList<Future<ByteBuffer>>();
    int sheeps = 0;

    Path path = Paths.get(Environments.USER_HOME, "a.txt");
    try (AsynchronousFileChannel channel =
        AsynchronousFileChannel.open(path, EnumSet.of(StandardOpenOption.READ), executorService)) {

      for (int i = 0; i < 50; i++) {
        Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {

          @Override
          public ByteBuffer call() throws Exception {
            ByteBuffer bb =
                ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
            channel.read(bb, ThreadLocalRandom.current().nextInt(0, 100));
            return bb;
          }
        };

        Future<ByteBuffer> future = executorService.submit(worker);
        futures.add(future);
      }

      Thread.sleep(5000L);
      // deny new works and wait for existing work done
      executorService.shutdown();

      if (!executorService.isTerminated()) {
        System.out.println("sheep: " + (++sheeps));
      }

      // read all future buffers
      for (Future<ByteBuffer> future : futures) {
        ByteBuffer bb = future.get();

        bb.flip();
        System.out.println(Charset.forName(Environments.DEFAULT_FILE_ENCODING).decode(bb));
        bb.clear();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
