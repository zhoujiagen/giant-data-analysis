package com.spike.giantdataanalysis.commons.io.nio.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

import com.spike.giantdataanalysis.commons.io.util.NIOs;

/**
 * ByteBuffer features test<br/>
 * byte buffer is the foundamental data structure of OS and IO devices.
 * @author zhoujiagen
 */
public class TestByteBuffer {

  @Test
  public void sizeOfPrimitiveType() {
    int BitsPerByte = 8;

    assertEquals(BitsPerByte, Byte.SIZE);
    assertEquals(2 * BitsPerByte, Character.SIZE);
    assertEquals(2 * BitsPerByte, Short.SIZE);
    assertEquals(4 * BitsPerByte, Integer.SIZE);
    assertEquals(8 * BitsPerByte, Long.SIZE);
    assertEquals(4 * BitsPerByte, Float.SIZE);
    assertEquals(8 * BitsPerByte, Double.SIZE);
  }

  /**
   * {@link ByteBuffer#order()} is meaningful to {@link ByteBuffer#getInt()} etc
   */
  @Test
  public void ordering() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);

    assertEquals(ByteOrder.BIG_ENDIAN, byteBuffer.order());// the default

    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    assertEquals(ByteOrder.LITTLE_ENDIAN, byteBuffer.order());

    int integer = 0x037FB4C7;
    byte[] bytes = NIOs.toBytes(integer);

    ByteBuffer byteBuffer2 = ByteBuffer.wrap(bytes);
    assertEquals("37fb4c7", NIOs.contentOfUnreadBuffer(byteBuffer2));

    byteBuffer2.order(ByteOrder.LITTLE_ENDIAN);
    assertEquals("c7b47f03", NIOs.contentOfUnreadBuffer(byteBuffer2));
  }

  /**
   * direct buffer is not allocated in JVM object heap
   */
  @Test
  public void direct() {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
    assertTrue(byteBuffer.isDirect());

    ByteBuffer byteBuffer2 = ByteBuffer.allocate(10);
    assertFalse(byteBuffer2.isDirect());

  }

  @Test
  public void charViewOfByteBuffer() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(ByteOrder.BIG_ENDIAN);
    CharBuffer charBuffer = byteBuffer.asCharBuffer();

    byteBuffer.put(0, (byte) 0);
    byteBuffer.put(1, (byte) 'H');
    byteBuffer.put(2, (byte) 0);
    byteBuffer.put(3, (byte) 'i');
    byteBuffer.put(4, (byte) 0);
    byteBuffer.put(5, (byte) '!');
    byteBuffer.put(6, (byte) 0);

    System.out.println(NIOs.detailsOfBuffer(byteBuffer));
    System.out.println(NIOs.detailsOfBuffer(charBuffer));
  }

  @Test
  public void elementViewOfByteBuffer() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);

    byteBuffer.put(0, (byte) 0x07);
    byteBuffer.put(1, (byte) 0x3B);
    byteBuffer.put(2, (byte) 0xC5);
    byteBuffer.put(3, (byte) 0x31);
    byteBuffer.put(4, (byte) 0x5E);
    byteBuffer.put(5, (byte) 0x94);
    byteBuffer.put(6, (byte) 0xD6);
    byteBuffer.put(6, (byte) 0x04);

    byteBuffer.position(1).limit(5);
    assertEquals("3bc5315e", Integer.toHexString(byteBuffer.order(ByteOrder.BIG_ENDIAN).getInt()));

    byteBuffer.position(1).limit(5);
    assertEquals("5e31c53b",
      Integer.toHexString(byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt()));

  }

  @Test
  public void unsigned() {

  }

  /**
   * see {@link FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}
   */
  @Test
  public void MappedByteBuffer() {
    // TODO
  }

  @Deprecated
  @Test
  public void navie() {
    assertTrue(true);
  }
}
