package com.spike.giantdataanalysis.commons.io.util;

import java.nio.ByteBuffer;

/**
 * support unsigned number for java primitive types
 * @author zhoujiagen
 */
public final class ByteBuffers {
  private static final int BYTE_PADDING = 0xFF;
  private static final int SHORT_PADDING = 0xFFFF;
  private static final int INT_PADDING = 0xFFFFFFFF;

  // byte - 8bits
  public static short getByte(ByteBuffer byteBuffer) {
    return (short) (byteBuffer.get() & BYTE_PADDING);
  }

  public static short getByte(ByteBuffer byteBuffer, int position) {
    return (short) (byteBuffer.get(position) & (short) BYTE_PADDING);
  }

  public static void putByte(ByteBuffer byteBuffer, int value) {
    byteBuffer.put((byte) (value & BYTE_PADDING));
  }

  public static void putByte(ByteBuffer byteBuffer, int position, int value) {
    byteBuffer.put(position, (byte) (value & BYTE_PADDING));
  }

  // short - 2 bytes
  public static int getShort(ByteBuffer byteBuffer) {
    return (byteBuffer.getShort() & SHORT_PADDING);
  }

  public static int getShort(ByteBuffer byteBuffer, int position) {
    return (byteBuffer.getShort(position) & SHORT_PADDING);
  }

  public static void putShort(ByteBuffer byteBuffer, int value) {
    byteBuffer.putShort((short) (value & SHORT_PADDING));
  }

  public static void putShort(ByteBuffer byteBuffer, int position, int value) {
    byteBuffer.putShort(position, (short) (value & SHORT_PADDING));
  }

  // int - 4 bytes
  public static long getInt(ByteBuffer byteBuffer) {
    return ((long) byteBuffer.getInt() & INT_PADDING);
  }

  public static long getInt(ByteBuffer byteBuffer, int position) {
    return ((long) byteBuffer.getInt(position) & INT_PADDING);
  }

  public static void putInt(ByteBuffer byteBuffer, long value) {
    byteBuffer.putInt((int) (value & INT_PADDING));
  }

  public static void putInt(ByteBuffer byteBuffer, int position, int value) {
    byteBuffer.putInt(position, (int) (value & INT_PADDING));
  }
}
