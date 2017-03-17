package com.spike.giantdataanalysis.netty.support;

import io.netty.util.CharsetUtil;

import com.google.common.base.Preconditions;

/**
 * @author zhoujiagen
 * @see com.google.common.primitives.Bytes
 */
public class Bytes {
  // ======================================== properties

  // ======================================== methods

  public static byte[] SUBSET(byte[] bytes, int offset, int length) {
    Preconditions.checkArgument(offset >= 0, "offset must great than or equal to 0");
    Preconditions.checkArgument(length > 0, "length must great than 0");
    Preconditions.checkArgument(offset + length <= bytes.length,
      "out of index boundary: %s + %s > %s", offset, length, bytes.length);

    byte[] result = new byte[length];
    System.arraycopy(bytes, offset, result, 0, length);
    return result;
  }

  /**
   * 获取字符串的字节表示
   * @param message
   * @return
   */
  public static byte[] WRAP(String message) {
    return message.getBytes(CharsetUtil.UTF_8);
  }

  /**
   * 获取字符的字节表示
   * @param message
   * @return
   */
  public static byte WRAP(char c) {
    return ((byte) c);
  }

  public static String STRING(byte[] bytes, int offset, int length) {
    return new String(SUBSET(bytes, offset, length));
  }

  public static String STRING(byte[] bytes) {
    // char[] char = new char[bytes.length];
    return new String(bytes);
  }

}
