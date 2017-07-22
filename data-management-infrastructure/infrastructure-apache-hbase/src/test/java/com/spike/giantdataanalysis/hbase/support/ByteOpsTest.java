package com.spike.giantdataanalysis.hbase.support;

import org.junit.Assert;
import org.junit.Test;

import static com.spike.giantdataanalysis.hbase.support.ByteOps.*;

public class ByteOpsTest {

  @Test
  public void _setBitValue() {
    // 测试设置位值
    byte b = 0;
    for (int i = 0; i <= 7; i++) {
      b = setBitValue(b, i, (byte) 1);
    }
    Assert.assertEquals("11111111", byteToBit(b));
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

    System.out.println(byteToBit(_1) + byteToBit(_2));

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

    Assert.assertEquals("00000010", byteToBit(bitCopy(_1, 2, 0, 2)));
    System.out.println(byteToBit(bitCopy(_1, 2, 0, 2)));
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
    System.out.println(byteToBit(_1)//
        + " " + byteToBit(_2)//
        + " " + byteToBit(_3)//
        + " " + byteToBit(_4));

    System.out.println(bitCopy(_4, 6, 0, 2));
    System.out.println(_3 << 2);
    System.out.println(_2 << 10);
    System.out.println((_1 & 0x0F) << 18);
    long value = bitCopy(_4, 6, 0, 2) + (_3 << 2) + (_2 << 10) + ((_1 & 0x0F) << 18);
    Assert.assertEquals(262147l, value);
    System.out.println(value);
  }

}
