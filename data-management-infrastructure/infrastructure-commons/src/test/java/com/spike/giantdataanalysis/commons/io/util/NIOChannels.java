package com.spike.giantdataanalysis.commons.io.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * NIO Channel utilities
 * @author zhoujiagen
 */
public class NIOChannels {

  /**
   * copy content of src to dest, <br/>
   * using {@link ByteBuffer#compact()} when inner ByteBuffer was not fully drained
   * @param src
   * @param dest
   * @throws IOException
   */
  public static void channelCopy1(ReadableByteChannel src, WritableByteChannel dest)
      throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16 * 1024);// 16KB

    while (src.read(byteBuffer) != -1) {
      byteBuffer.flip();// prepare to be drained

      dest.write(byteBuffer);// may block!!!

      byteBuffer.compact();// in case of byteBuffer is not fully drained
    }

    // now byteBuffer is in fill state
    byteBuffer.flip();

    // make sure byteBuffer is fully drained
    while (byteBuffer.hasRemaining()) {
      dest.write(byteBuffer);
    }
  }

  /**
   * copy content of src to dest, <br/>
   * using {@link ByteBuffer#clear()} to maker user byteBuffer was fully drained, while this may
   * cause more SYSTEM CALLS
   * @param src
   * @param dest
   * @throws IOException
   */
  public static void channelCopy2(ReadableByteChannel src, WritableByteChannel dest)
      throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16 * 1024);// 16KB

    while (src.read(byteBuffer) != -1) {
      byteBuffer.flip();

      while (byteBuffer.hasRemaining()) {
        dest.write(byteBuffer);
      }

      // make sure byteBuffer is empty, and ready for filling
      byteBuffer.clear();
    }
  }

}
