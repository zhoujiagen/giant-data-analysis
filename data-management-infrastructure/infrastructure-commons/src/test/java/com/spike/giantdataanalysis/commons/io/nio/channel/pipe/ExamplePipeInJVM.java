package com.spike.giantdataanalysis.commons.io.nio.channel.pipe;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

import com.spike.giantdataanalysis.commons.io.util.Environments;

/**
 * Pipe wrapped a source and a sink channel<br/>
 * can only be used in a single JVM, <br/>
 * direction: sink(writable) --> source(readable)
 * @author zhoujiagen
 */
public class ExamplePipeInJVM {
  public static void main(String[] args) throws Exception {
    WritableByteChannel out = Channels.newChannel(System.out);

    ReadableByteChannel workerChannel = startWorker(10);

    ByteBuffer bb = ByteBuffer.allocate(100);
    while (workerChannel.read(bb) >= 0) {
      bb.flip();
      out.write(bb);
      bb.clear();
    }

    // clean up
    workerChannel.close();
    out.close();
  }

  private static ReadableByteChannel startWorker(int count) throws Exception {
    Pipe pipe = Pipe.open();

    Worker worker = new Worker(pipe.sink(), count);
    worker.start();

    return pipe.source();
  }

  /**
   * Work write to Pipe' SinkChannel
   * @author zhoujiagen
   */
  private static class Worker extends Thread {
    WritableByteChannel channel;
    int count;

    public Worker(WritableByteChannel channel, int count) {
      this.channel = channel;
      this.count = count;
    }

    @Override
    public void run() {
      ByteBuffer bb = ByteBuffer.allocate(100);

      try {
        for (int i = 0; i < this.count; i++) {
          fill(bb);
          while (channel.write(bb) > 0)
            ;
        }

        this.channel.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    private String[] products = { "a", "b", "c", "d", "e" };
    private Random random = new Random();

    private void fill(ByteBuffer buffer) {
      buffer.clear();
      buffer.put(products[random.nextInt(products.length)].getBytes());
      buffer.put(Environments.NEWLINE.getBytes());
      buffer.flip();

    }
  }

}
