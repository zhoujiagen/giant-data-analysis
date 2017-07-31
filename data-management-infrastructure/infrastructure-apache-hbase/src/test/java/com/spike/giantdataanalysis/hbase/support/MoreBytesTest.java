package com.spike.giantdataanalysis.hbase.support;

import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

import static com.spike.giantdataanalysis.hbase.support.MoreBytes.*;

public class MoreBytesTest {

  @Test
  public void _number_size() {
    // 1B
    System.out.println(Byte.SIZE); // 8

    // 2B
    System.out.println(Short.SIZE);// 16
    System.out.println(Character.SIZE);// 16

    // 4B
    System.out.println(Integer.SIZE);// 32
    System.out.println(Float.SIZE);// 32

    // 8B
    System.out.println(Long.SIZE);// 64
    System.out.println(Double.SIZE);// 64
  }

  // 数值的字节表示
  @Test
  public void _number_bytes() {
    System.out.println("byte>>>");
    byte _byte = fromBitArray(b1, b0, b0, b0);
    System.out.println(_byte);

    System.out.println("\nshort>>>");
    byte[] _short_bytes = new byte[] { fromBitArray(b1, b0, b0, b0), fromBitArray(b1, b0, b0, b0) };
    System.out.println(asBitString(_short_bytes[0]) + asBitString(_short_bytes[1]));
    short _short = (short) (_short_bytes[0] * FACTOR_8 + _short_bytes[1]);
    System.out.println(Integer.toString(_short, 2));// 二进制内容的字符串

    System.out.println("\nint/float>>>");
    int _int = Integer.MAX_VALUE;
    float _float = Float.MAX_VALUE;
    System.out.println(_int);
    System.out.println(Integer.toBinaryString(_int));// 二进制内容的字符串
    System.out.println(Integer.parseInt(Integer.toBinaryString(_int), 2));
    System.out.println(_float);
    int _float_int = Float.floatToIntBits(_float);
    System.out.println(_float_int);
    System.out.println(Float.intBitsToFloat(_float_int));

    System.out.println("\nlong/double>>>");
    long _long = Long.MAX_VALUE;
    double _double = Double.MAX_VALUE;
    System.out.println(_long);
    System.out.println(Long.toBinaryString(_long));// 二进制内容的字符串
    System.out.println(Long.parseLong(Long.toBinaryString(_long), 2));
    System.out.println(_double);
    long _double_long = Double.doubleToLongBits(_double);
    System.out.println(_double_long);
    System.out.println(Double.longBitsToDouble(_double_long));
  }

  // 测试BitSet
  @Test
  public void _test_BitSet() {
    BitSet bitSet = new BitSet(8);
    bitSet.set(0);
    bitSet.set(1);
    bitSet.set(2, false);
    bitSet.set(3, false);
    bitSet.set(4, false);
    bitSet.set(5, false);
    bitSet.set(6);
    bitSet.set(7, false);
    System.out.println(asString(bitSet));
  }

  @Test
  public void _setBitValue() {
    // 测试设置位值
    byte b = 0;
    for (int i = 0; i <= 7; i++) {
      b = setBitValue(b, i, (byte) 1);
    }
    Assert.assertEquals("11111111", asBitString(b));
  }

  @Test
  public void bitShift() {
    // 测试移位操作
    byte _1 = 0;
    byte _2 = 0;

    // [0010 0001 0011] 1010
    _1 = setBitValue(_1, 0, b1);
    _1 = setBitValue(_1, 5, b1);

    _2 = setBitValue(_2, 1, b1);
    _2 = setBitValue(_2, 3, b1);
    _2 = setBitValue(_2, 4, b1);
    _2 = setBitValue(_2, 5, b1);

    System.out.println(asBitString(_1) + asBitString(_2));

    // _1 * (2^4)
    System.out.println(_1 * Math.pow(2, 4));
    System.out.println(bitCopy(_2, 4, 0, 4));
    long deltaSeconds = (long) (_1 * Math.pow(2, 4) + bitCopy(_2, 4, 0, 4));
    byte flag = (byte) (_2 & 0x0F);
    System.out.println(deltaSeconds);
    System.out.println(flag);
  }

  @Test
  public void _bitCopy() {
    byte _1 = b0;
    _1 = setBitValue(_1, 0, b1);
    _1 = setBitValue(_1, 1, b1);
    _1 = setBitValue(_1, 3, b1);

    Assert.assertEquals("00000010", asBitString(bitCopy(_1, 2, 0, 2)));
    System.out.println(asBitString(bitCopy(_1, 2, 0, 2)));
  }

  @Test
  public void _bit_fragment() {
    // 测试获取片段的值
    byte _1 = b0;
    byte _2 = b0;
    byte _3 = b0;
    byte _4 = b0;

    _1 = setBitValue(_1, 0, b1);
    _4 = setBitValue(_4, 6, b1);
    _4 = setBitValue(_4, 7, b1);
    System.out.println(asBitString(_1)//
        + " " + asBitString(_2)//
        + " " + asBitString(_3)//
        + " " + asBitString(_4));

    System.out.println(bitCopy(_4, 6, 0, 2));
    System.out.println(_3 << 2);
    System.out.println(_2 << 10);
    System.out.println((_1 & 0x0F) << 18);
    long value = bitCopy(_4, 6, 0, 2) + (_3 << 2) + (_2 << 10) + ((_1 & 0x0F) << 18);
    Assert.assertEquals(262147l, value);
    System.out.println(value);
  }

  @Test
  public void _fromByteArray() {
    byte[] bits = new byte[] { b1, b0, b0, b0 };
    byte result = MoreBytes.fromBitArray(bits);
    Assert.assertEquals("00000001", asBitString(result));

    bits = new byte[] { b1, b0, b0, b0, b0, b1, b0, b0, b0 };
    result = MoreBytes.fromBitArray(bits);
    Assert.assertEquals("00100001", asBitString(result));
  }

}
