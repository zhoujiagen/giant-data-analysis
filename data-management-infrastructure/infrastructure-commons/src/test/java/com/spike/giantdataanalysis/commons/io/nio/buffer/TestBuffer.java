package com.spike.giantdataanalysis.commons.io.nio.buffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.spike.giantdataanalysis.commons.io.util.NIOs;

/**
 * BufferTest <br/>
 * <b>Buffer is not thread safe.</b><br/>
 * <ul>
 * <li>CharBuffer</li>
 * <li>IntBuffer</li>
 * <li>DoubleBuffer</li>
 * <li>ShortBuffer</li>
 * <li>LongBuffer</li>
 * <li>FloatBuffer</li>
 * <li>ByteBuffer</li>
 * <li>MappedByteBuffer</li>
 * </ul>
 * @author zhoujiagen
 */
public class TestBuffer {

  @Test
  public void mark() {
    // rewind(), clear(), flip() drop marks
    // new value is smaller than current mark, limit(i) and positon(i) will
    // drop marks
    CharBuffer charBuffer = CharBuffer.allocate(10);
    charBuffer.append("Mellow");
    assertEquals(6, charBuffer.position());
    assertEquals(10, charBuffer.limit());

    charBuffer.flip();

    charBuffer.position(2).mark().position(4);// "ow" left
    charBuffer.reset();// "llow" left
    assertEquals(2, charBuffer.position());
  }

  @Test
  public void compact() {
    CharBuffer charBuffer = CharBuffer.allocate(10);
    charBuffer.append("Mellow");
    assertEquals(6, charBuffer.position());
    assertEquals(10, charBuffer.limit());

    charBuffer.flip();

    charBuffer.get();
    charBuffer.get();
    assertEquals(2, charBuffer.position());// "[Me]llow"

    int unReadCharNumber = "Mellow".length() - 2;
    charBuffer.compact();
    assertEquals(unReadCharNumber, charBuffer.position());// "[llow]ow"
    assertEquals(10, charBuffer.limit());
  }

  @Test
  public void clear() {
    // clear() -> position = 0, limit = capacity, no content modified
    char[] charArray = "012345".toCharArray();
    CharBuffer charBuffer = CharBuffer.wrap(charArray);
    for (int i = 0; i < charBuffer.limit(); i++) {
      charBuffer.get();
    }
    charBuffer.clear();
    assertEquals(0, charBuffer.position());
    assertEquals(6, charBuffer.limit());
    assertEquals('3', charBuffer.get(3));
  }

  @Test
  public void testRemaining() {
    // hasRemaing()
    byte[] wrapedByteArray = new byte[] { 0, 1, 2, 3, 4, 5 };
    ByteBuffer byteBuffer = ByteBuffer.wrap(wrapedByteArray);
    byte[] myByteArray = new byte[wrapedByteArray.length];
    for (int i = 0; byteBuffer.hasRemaining(); i++) {
      myByteArray[i] = byteBuffer.get();
    }
    assertArrayEquals(myByteArray, wrapedByteArray);

    // remaining()
    ByteBuffer byteBuffer2 = ByteBuffer.wrap(wrapedByteArray);
    int pos = 2;
    byteBuffer2.position(pos);
    int leftCount = byteBuffer2.remaining();
    assertEquals(4, leftCount);
  }

  @Test
  public void flip() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    // Hello
    byteBuffer.put((byte) 'H').put((byte) 'e').put((byte) 'l').put((byte) 'l').put((byte) 'o');
    assertEquals(5, byteBuffer.position());

    // change to Mellow
    byteBuffer.put(0, (byte) 'M').put((byte) 'w');
    assertEquals("Mellow", NIOs.contentOfBuffer(byteBuffer));

    // now flip the buffer to be read
    assertEquals("Mellow".length(), byteBuffer.position());
    assertEquals(10, byteBuffer.limit());
    byteBuffer.flip();
    assertEquals(0, byteBuffer.position());
    assertEquals("Mellow".length(), byteBuffer.limit());
    // rewind() only set position = 0
  }

  /**
   * same as flip, except that only set position to 0<br/>
   * see {@link flip}
   */
  @Test
  public void rewind() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    byteBuffer.put((byte) 'H').put((byte) 'e').put((byte) 'l').put((byte) 'l').put((byte) 'o');
    assertEquals(5, byteBuffer.position());
    assertEquals(10, byteBuffer.limit());

    byteBuffer.rewind();
    assertEquals(0, byteBuffer.position());
    assertEquals(10, byteBuffer.limit());// same as before
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getAndPutWithException() {
    IntBuffer intBuffer = IntBuffer.wrap(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
    intBuffer.get(11);
    // intBuffer.put(11, 11);

    // TODO case with ReadOnlyBufferException
  }

  @Test
  public void putAndGet() {
    IntBuffer intBuffer = IntBuffer.wrap(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
    assertEquals(1, intBuffer.get(0));

    assertEquals(0, intBuffer.position());
    intBuffer.put(11);
    assertEquals(11, intBuffer.get(0));
    assertEquals(1, intBuffer.position());
  }

  @Test
  public void basicPropertiesAndOperation() {
    int[] intArray = new int[100];
    IntBuffer intBuffer = IntBuffer.wrap(intArray);

    // capacity
    assertEquals(100, intArray.length);

    // position
    assertEquals(0, intBuffer.position());

    // limit
    assertEquals(100, intBuffer.limit());

    // mark
    // mark() -> mark = position, reset() -> position = mark
    // 0 <= mark <= position <= limit <= capacity
    intBuffer.mark();// mark = position = 0
    intBuffer.position(5);
    assertEquals(5, intBuffer.position());
    intBuffer.reset();// position = mark = 0
    assertEquals(0, intBuffer.position());

    intBuffer.mark().position(5).reset();// Builder: chain calling
    assertEquals(0, intBuffer.position());
  }

  @Test
  public void create() {
    CharBuffer charBuffer = CharBuffer.allocate(100);// 1
    assertEquals(100, charBuffer.capacity());
    assertEquals(100, charBuffer.limit());
    assertEquals(0, charBuffer.position());

    char[] myArray = new char[100];
    CharBuffer charBuffer2 = CharBuffer.wrap(myArray);// 2
    assertEquals(100, charBuffer2.capacity());
    charBuffer2 = CharBuffer.wrap(myArray, 12, 42);// 2'
    assertEquals(100, charBuffer2.capacity());
    assertEquals(12, charBuffer2.position());
    assertEquals(12 + 42, charBuffer2.limit());

    String greeting = "Hello Java NIO";
    CharBuffer charBuffer3 = CharBuffer.wrap(greeting);// 3
    assertEquals(greeting.length(), charBuffer3.capacity());
  }

  @Test
  public void hasRemaining() {
    CharBuffer charBuffer = CharBuffer.allocate(10);
    for (int i = 0; i < 10; i++) {
      charBuffer.put((char) i);
    }
    assertFalse(charBuffer.hasRemaining());
  }

  @Test
  public void remaining() {
    CharBuffer charBuffer = CharBuffer.allocate(10);
    for (int i = 0; i < 5; i++) {
      charBuffer.put((char) i);
    }
    // limit() - position()
    assertEquals(5, charBuffer.remaining());
  }

  @Test
  public void reset() {
    IntBuffer intBuffer = IntBuffer.wrap(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
    intBuffer.position(2).mark().position(4);
    assertEquals(4, intBuffer.position());

    intBuffer.reset();
    assertEquals(2, intBuffer.position());
  }

  @Test
  public void shouldEqual() {
    String s1 = "dotcom job";
    CharBuffer charBuffer1 = CharBuffer.wrap(s1.toCharArray());

    String s2 = "com3dy";
    CharBuffer charBuffer2 = CharBuffer.wrap(s2.toCharArray());

    charBuffer1.position(3).limit(6);
    charBuffer2.limit(3);

    assertTrue(charBuffer1.equals(charBuffer2));
    assertTrue(charBuffer1.compareTo(charBuffer2) == 0);
  }

  @Test
  public void shouldNotEqual() {
    String s1 = "com3dy";
    CharBuffer charBuffer1 = CharBuffer.wrap(s1.toCharArray());
    CharBuffer charBuffer2 = CharBuffer.wrap(s1.toCharArray());

    charBuffer1.limit(6);
    charBuffer2.position(2).limit(6);

    assertFalse(charBuffer1.equals(charBuffer2));
    assertTrue(charBuffer1.compareTo(charBuffer2) < 0);
  }

  @Test
  public void batchMoveUsingBigArray() {
    String string = "what is rational is real, and what is real is rational.";
    char[] bigArray = new char[1000];

    CharBuffer charBuffer = CharBuffer.wrap(string.toCharArray());
    int length = charBuffer.remaining();
    charBuffer.get(bigArray, 0, length);

    assertEquals(0, bigArray[length]);

    assertEquals(string, NIOs.contentOfCharArray(bigArray, 0, length));
  }

  @Test
  public void batchMoveUsingSmallArray() {
    String string = "what is rational is real, and what is real is rational.";
    char[] smallArray = new char[5];
    CharBuffer charBuffer = CharBuffer.wrap(string.toCharArray());

    StringBuffer sb = new StringBuffer();
    while (charBuffer.hasRemaining()) {
      int length = Math.min(smallArray.length, charBuffer.remaining());
      charBuffer.get(smallArray, 0, length);
      for (int i = 0; i < length; i++) {// mock process the data
        sb.append(smallArray[i]);
      }
    }

    assertEquals(string, sb.toString());
  }

  @Test
  public void duplicate() {
    CharBuffer charBuffer = CharBuffer.wrap("0123456789".toCharArray());
    charBuffer.position(3).limit(6).mark().position(5);

    CharBuffer dupCharBuffer = charBuffer.duplicate();

    // each own's position/limit/mark
    charBuffer.clear();
    assertEquals(5, dupCharBuffer.position());

    // share same data
    dupCharBuffer.put('x');
    assertEquals('x', charBuffer.get(5));
  }

  @Test
  public void slice() {
    CharBuffer charBuffer = CharBuffer.allocate(100);
    charBuffer.position(12).limit(21);

    CharBuffer slicedBuffer = charBuffer.slice();
    assertEquals(21 - 12, slicedBuffer.capacity());
  }

  @Deprecated
  @Test
  public void navie() {
    Assert.assertTrue(true);
  }
}
