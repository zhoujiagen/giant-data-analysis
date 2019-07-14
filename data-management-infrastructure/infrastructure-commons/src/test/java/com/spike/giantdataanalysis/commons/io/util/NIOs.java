package com.spike.giantdataanalysis.commons.io.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * NIO utilities
 * @author zhoujiagen
 */
public class NIOs {
  /**
   * get bytes of string using a given character set
   * @param string
   * @param charset
   * @return
   * @throws Exception
   */
  public static byte[] bytes(String string, String charset) throws Exception {
    return string.getBytes(charset);
  }

  /**
   * get content of a byte buffer, no side effect
   * @param buffer
   * @return
   */
  public static final String contentOfBuffer(final ByteBuffer buffer) {
    int position = buffer.position();
    int limit = buffer.limit();
    buffer.flip();

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buffer.limit(); i++) {// 0 to current position
      sb.append(Integer.toHexString(buffer.get(i)));
    }

    // insist no side effect
    buffer.position(position);
    buffer.limit(limit);

    return sb.toString();
  }

  /**
   * content of unread byte buffer, hex representation using {@link ByteBuffer#getInt()}
   * @param buffer
   * @return
   */
  public static final String contentOfUnreadBuffer(final ByteBuffer buffer) {
    int position = buffer.position();
    int limit = buffer.limit();

    StringBuffer sb = new StringBuffer();
    while (buffer.hasRemaining()) {
      sb.append(Integer.toHexString(buffer.getInt()));
    }

    // insist no side effect
    buffer.position(position);
    buffer.limit(limit);

    return sb.toString();
  }

  public static String contentOfBuffer(final CharBuffer buffer) {
    int position = buffer.position();
    int limit = buffer.limit();
    buffer.flip();

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buffer.limit(); i++) {// 0 to current position
      sb.append(buffer.get(i));
    }

    buffer.position(position);
    buffer.limit(limit);

    return sb.toString();

  }

  public static String contentOfCharArray(char[] array, int start, int length) {
    if (array == null || start + length > array.length) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = start; i < length; i++) {
      sb.append(array[start + i]);
    }
    return sb.toString();
  }

  public static String contentOfByteArray(byte[] array, int start, int length) {
    if (array == null || start + length > array.length) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = start; i < length; i++) {
      sb.append(Integer.toHexString(array[start + i]));
    }
    return sb.toString();
  }

  /**
   * Convert integer to byte array<br/>
   * sample: 0x037fb4c7 => {03, 7f, b4, c7}
   * @param integer
   * @return
   */
  public static final byte[] toBytes(int integer) {
    byte[] result = new byte[4];

    result[0] = (byte) (integer >> 24);
    result[1] = (byte) (integer >> 16);
    result[2] = (byte) (integer >> 8);
    result[3] = (byte) (integer /* >> 0 */);

    return result;

    // return BigInteger.valueOf(integer).toByteArray();
  }

  /**
   * properties and content of a buffer
   * @param buffer
   */
  public static String detailsOfBuffer(Buffer buffer) {
    StringBuffer sb = new StringBuffer();

    sb.append("position=");
    sb.append(buffer.position());
    sb.append(", limit=");
    sb.append(buffer.limit());
    sb.append(", capacity=");
    sb.append(buffer.capacity());
    sb.append(": ").append(buffer.toString());

    return sb.toString();
  }
}
