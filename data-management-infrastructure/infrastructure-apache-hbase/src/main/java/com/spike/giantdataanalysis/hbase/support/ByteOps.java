package com.spike.giantdataanalysis.hbase.support;

import com.google.common.base.Preconditions;

public class ByteOps {

  /** 位 */
  public static final byte[] BITS = new byte[] { (byte) 0, (byte) 1 };

  public static final byte b0 = (byte) 0;
  public static final byte b1 = (byte) 1;

  /**
   * 位拷贝, 注意pos从右向左
   * <p>
   * 例 src = 0000 1011, srcPos = 2, desPos = 0, length = 2, 则dest = 0000 0010
   * @see System#arraycopy(Object, int, Object, int, int)
   */
  public static byte bitCopy(byte src, int srcPos, int destPos, int length) {
    Preconditions.checkArgument(srcPos + length <= Byte.SIZE, "out of boundary!");

    byte dest = b0;
    for (int i = 0; i < length; i++) {
      if (destPos + length < Byte.SIZE) {
        dest = setBitValue(dest, destPos + i, getBitValue(src, srcPos + i));
      }
    }
    return dest;
  }

  /**
   * 获取运算数指定位置的值
   * <p>
   * 例如： 0000 1011 获取其第 0 位的值为 1, 第 2 位 的值为 0<br>
   * @param source 需要运算的数
   * @param pos 指定位置 (0<=pos<=7)
   * @return 指定位置的值(0 or 1)
   */
  public static byte getBitValue(byte source, int pos) {
    return (byte) ((source >> pos) & 1);
  }

  /**
   * 将运算数指定位置的值置为指定值
   * <p>
   * 例: 0000 1011 需要更新为 0000 1111, 即第 2 位的值需要置为 1<br>
   * @param source 需要运算的数
   * @param pos 指定位置 (0<=pos<=7)
   * @param value 只能取值为 0, 或 1, 所有大于0的值作为1处理, 所有小于0的值作为0处理
   * @return 运算后的结果数
   */
  public static byte setBitValue(byte source, int pos, byte value) {

    byte mask = (byte) (1 << pos);
    if (value > 0) {
      source |= mask;
    } else {
      source &= (~mask);
    }

    return source;
  }

  /**
   * 将运算数指定位置取反值
   * <p>
   * 例： 0000 1011 指定第 3 位取反, 结果为 0000 0011; 指定第2位取反, 结果为 0000 1111<br>
   * @param source
   * @param pos 指定位置 (0<=pos<=7)
   * @return 运算后的结果数
   */
  public static byte reverseBitValue(byte source, int pos) {
    byte mask = (byte) (1 << pos);
    return (byte) (source ^ mask);
  }

  /**
   * 检查运算数的指定位置是否为1
   * @param source 需要运算的数
   * @param pos 指定位置 (0<=pos<=7)
   * @return true 表示指定位置值为1, false 表示指定位置值为 0
   */
  public static boolean checkBitValue(byte source, int pos) {
    source = (byte) (source >>> pos);

    return (source & 1) == 1;
  }

  public static byte[] toBitArray(byte b) {
    byte[] array = new byte[8];
    for (int i = 7; i >= 0; i--) {
      array[i] = (byte) (b & 1);
      b = (byte) (b >> 1);
    }
    return array;
  }

  /**
   * 把byte转为字符串的bit
   */
  public static String byteToBit(byte b) {
    return "" //
        + (byte) ((b >> 7) & 0x1)//
        + (byte) ((b >> 6) & 0x1)//
        + (byte) ((b >> 5) & 0x1)//
        + (byte) ((b >> 4) & 0x1)//
        + (byte) ((b >> 3) & 0x1) //
        + (byte) ((b >> 2) & 0x1) //
        + (byte) ((b >> 1) & 0x1) //
        + (byte) ((b >> 0) & 0x1);
  }

  public static byte decodeBinaryString(String byteStr) {
    int re, len;
    if (null == byteStr) {
      return 0;
    }
    len = byteStr.length();
    if (len != 4 && len != 8) {
      return 0;
    }
    if (len == 8) {// 8 bit处理
      if (byteStr.charAt(0) == '0') {// 正数
        re = Integer.parseInt(byteStr, 2);
      } else {// 负数
        re = Integer.parseInt(byteStr, 2) - 256;
      }
    } else {// 4 bit处理
      re = Integer.parseInt(byteStr, 2);
    }
    return (byte) re;
  }
}
