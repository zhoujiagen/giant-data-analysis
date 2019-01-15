package com.spike.giantdataanalysis.commons.io.nio.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

/**
 * File Lock, run with:<br/>
 * -w /tmp/locktest.dat (1 instancei and <br/>
 * -r /tmp/locktest.dat (* instances)
 * @author zhoujiagen
 */
public class TestFileLock {
  private static final int SIZEOF_INT = 4;
  private static final int INDEX_START = 0;
  private static final int INDEX_COUNT = 10;
  private static final int INDEX_SIZE = INDEX_COUNT * SIZEOF_INT;

  private ByteBuffer byteBuffer = ByteBuffer.allocate(INDEX_SIZE);
  // a integer view of byte buffer
  private IntBuffer intBuffer = byteBuffer.asIntBuffer();

  private Random random = new Random();

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage:  [-r | -w] filename");
      return;
    }

    boolean writeFlag = false;

    writeFlag = args[0].equals("-w");
    String fileName = args[1];

    RandomAccessFile raf = new RandomAccessFile(fileName, (writeFlag ? "rw" : "r"));
    FileChannel fc = raf.getChannel();

    TestFileLock service = new TestFileLock();
    if (writeFlag) {
      service.doUpdate(fc);
    } else {
      service.doQueries(fc);
    }

    // clean up
    fc.close();
    raf.close();
  }

  /**
   * reader
   * @param fc file channel
   */
  private void doQueries(FileChannel fc) throws Exception {
    while (true) {
      System.out.println("try for shared lock...");

      FileLock fileLock = null;
      try {
        // shared lock: true
        fileLock = fc.lock(INDEX_START, INDEX_SIZE, true);

        int count = random.nextInt(60) + 20;
        for (int i = 0; i < count; i++) {
          int n = random.nextInt(INDEX_COUNT);
          int position = INDEX_START + SIZEOF_INT * n;

          byteBuffer.clear();
          fc.read(byteBuffer, position);

          int value = intBuffer.get(n);
          System.out.println("Index entry " + n + "=" + value);

          Thread.sleep(100L);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        fileLock.release();
      }

      System.out.println("<sleeping>");
      Thread.sleep(random.nextInt(3000) + 500);
    }
  }

  /**
   * writer
   * @param fc file channel
   */
  private void doUpdate(FileChannel fc) throws Exception {
    while (true) {
      System.out.println("try for exclusive lock...");
      FileLock fileLock = null;
      try {
        fileLock = fc.lock(INDEX_START, INDEX_SIZE, false);

        updateIndex(fc);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        fileLock.release();
      }

      System.out.println("<sleeping>");
      Thread.sleep(random.nextInt(3000) + 500);
    }
  }

  private int indexValue = 1;

  /**
   * write new data to index slots
   * @param fc file channel
   */
  private void updateIndex(FileChannel fc) throws Exception {
    intBuffer.clear();

    for (int i = 0; i < INDEX_COUNT; i++) {
      indexValue++;
      System.out.println("Updating index " + i + "=" + indexValue);
      intBuffer.put(indexValue);

      Thread.sleep(500L);
    }

    byteBuffer.clear();// prepare to write to file channel
    fc.write(byteBuffer, INDEX_START);
  }
}
