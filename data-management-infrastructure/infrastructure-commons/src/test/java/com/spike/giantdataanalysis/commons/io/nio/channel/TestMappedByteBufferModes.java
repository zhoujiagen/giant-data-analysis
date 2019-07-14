package com.spike.giantdataanalysis.commons.io.nio.channel;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * MapMode of MappedByteBuffer
 * @author zhoujiagen
 */
public class TestMappedByteBufferModes {
  public static void main(String[] args) throws Exception {
    File tempFile = File.createTempFile("mmaptest", null);
    RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
    FileChannel fc = raf.getChannel();

    ByteBuffer bb = ByteBuffer.allocate(100);
    bb.put("This is the file content".getBytes());
    bb.flip();
    fc.write(bb, 0);

    bb.clear();
    bb.put("This is more file content".getBytes());
    bb.flip();
    fc.write(bb, 8 * 1024);

    long fileSize = fc.size();
    MappedByteBuffer ro = fc.map(MapMode.READ_ONLY, 0, fileSize);
    MappedByteBuffer rw = fc.map(MapMode.READ_WRITE, 0, fileSize);
    MappedByteBuffer cow = fc.map(MapMode.PRIVATE, 0, fileSize);// copy-on-write

    System.out.println("Begin");
    showBuffer(ro, rw, cow);

    System.out.println("Change to COW buffer");
    cow.position(8);
    cow.put("COW".getBytes());
    showBuffer(ro, rw, cow);

    System.out.println("Change to R/W buffer");
    rw.position(9);
    rw.put(" R/W ".getBytes());
    rw.position(8194);
    rw.put(" R/W ".getBytes());
    rw.force();
    showBuffer(ro, rw, cow);

    System.out.println("Write on channel");
    bb.clear();
    bb.put("Channel write ".getBytes());
    bb.flip();
    fc.write(bb, 0);
    bb.rewind();
    fc.write(bb, 8202);
    showBuffer(ro, rw, cow);

    System.out.println("Second hange to COW buffer");
    cow.position(8207);
    cow.put(" COW2 ".getBytes());
    showBuffer(ro, rw, cow);

    System.out.println("Second change to R/W buffer");
    rw.position(0);
    rw.put(" R/W2 ".getBytes());
    rw.position(8210);
    rw.put(" R/W2 ".getBytes());
    rw.force();
    showBuffer(ro, rw, cow);

    // clean up
    fc.close();
    raf.close();
    tempFile.delete();
  }

  private static void showBuffer(ByteBuffer ro, ByteBuffer rw, ByteBuffer cow) {
    dumpBuffer("R/O", ro);
    dumpBuffer("R/W", rw);
    dumpBuffer("COW", cow);
    System.out.println();
  }

  private static void dumpBuffer(String prefix, ByteBuffer buffer) {
    System.out.print(prefix + ": '");
    int nulls = 0;
    int limit = buffer.limit();

    for (int i = 0; i < limit; i++) {
      char c = (char) buffer.get(i);// absolute position
      if (c == '\u0000') {
        nulls++;
        continue;
      }

      if (nulls != 0) {
        System.out.print("|[" + nulls + " nulls]|");
        nulls = 0;
      }
      System.out.print(c);
    }

    System.out.println("'");
  }
}
