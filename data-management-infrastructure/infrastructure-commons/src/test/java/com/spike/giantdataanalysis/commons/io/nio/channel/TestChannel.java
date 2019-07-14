package com.spike.giantdataanalysis.commons.io.nio.channel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.spike.giantdataanalysis.commons.io.nio.channel.pipe.ExamplePipeInJVM;
import com.spike.giantdataanalysis.commons.io.nio.channel.socket.ExampleDatagramChannelClient;
import com.spike.giantdataanalysis.commons.io.nio.channel.socket.ExampleDatagramChannelServer;
import com.spike.giantdataanalysis.commons.io.nio.channel.socket.ExampleGreetingServerScoketChannel;
import com.spike.giantdataanalysis.commons.io.nio.channel.socket.ExampleGreetingSocketChannel;
import com.spike.giantdataanalysis.commons.io.util.Environments;
import com.spike.giantdataanalysis.commons.io.util.NIOChannels;

/**
 * Channel is a middle man between ByteBuffer and an entity(a file or a socket),<br/>
 * play the role of transfer data
 * @author zhoujiagen
 */
public class TestChannel {

  /**
   * read and write channel Note: ByteChannel is also ReadableByteChannel and WritableByteChannel
   * @throws IOException
   */
  @Test
  public void readAndWrite() throws IOException {
    ReadableByteChannel source = Channels.newChannel(System.in);
    WritableByteChannel target = Channels.newChannel(System.out);

    NIOChannels.channelCopy1(source, target);
    // ChannelUtil.channelCopy2(source, target);

    source.close();
    target.close();
  }

  /**
   * demonstration of GatheringByteChannel and ScatteringByteChannel,<br/>
   * Note: the core entity is the Channel
   */
  @Test
  public void gatherAndScatter() throws Exception {
    String outputFilePath = "temp/blahblah.txt";
    int reps = 10;

    FileOutputStream fos = new FileOutputStream(outputFilePath);
    GatheringByteChannel gatherChannel = fos.getChannel();// ~
    ByteBuffer[] bs = generateByteBuffers(reps);

    while (gatherChannel.write(bs) > 0) {
      System.err.println(".");
    }

    fos.close();
  }

  private static String[] col1 = { "10", "11", "12", "13", "14", "15", "16", "17", "18" };
  private static String[] col2 = { "20", "21", "22", "23", "24", "25", "26", "27", "28" };
  private static String[] col3 = { "30", "31", "32", "33", "34", "35", "36", "37", "38" };
  private static Random random = new Random();

  private ByteBuffer[] generateByteBuffers(int number) throws Exception {
    List<ByteBuffer> list = new ArrayList<ByteBuffer>();

    for (int i = 0; i < number; i++) {
      list.add(generateByteBuffer(col1, Environments.TAB));
      list.add(generateByteBuffer(col2, Environments.TAB));
      list.add(generateByteBuffer(col3, Environments.NEWLINE));
    }

    ByteBuffer[] result = new ByteBuffer[list.size()];
    list.toArray(result);

    return result;
  }

  private static ByteBuffer generateByteBuffer(String[] sources, String suffix) throws Exception {
    String pickedString = sources[random.nextInt(sources.length)];
    int total = pickedString.length() + suffix.length();

    ByteBuffer result = ByteBuffer.allocate(total);
    result.put(pickedString.getBytes("US-ASCII"));
    result.put(suffix.getBytes("US-ASCII"));

    result.flip();// prepare to be drained

    return result;
  }

  /**
   * in Unbuntu run:<br/>
   * $strings tempxxx.txt
   * @throws Exception
   */
  @Test
  public void fileChannelWithFileHole() throws Exception {
    // temp file in system tmp directory
    File temp = File.createTempFile("temp", ".txt");
    RandomAccessFile file = new RandomAccessFile(temp, "rw");
    FileChannel fileChannel = file.getChannel();

    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
    putData(fileChannel, 0, byteBuffer);
    putData(fileChannel, 5000000, byteBuffer);
    putData(fileChannel, 50000, byteBuffer);

    // renderer file's size
    System.out.println("Temp File: " + temp.getPath() + ", size = " + fileChannel.size() + " B.");

    fileChannel.close();
    file.close();
  }

  private static void putData(FileChannel fileChannel, int position, ByteBuffer byteBuffer)
      throws Exception {
    String string = "*<-- location " + position;

    byteBuffer.clear();
    byteBuffer.put(string.getBytes("US-ASCII"));
    byteBuffer.flip();

    fileChannel.position(position);
    fileChannel.write(byteBuffer);
  }

  /**
   * FileChannel share property 'position' with underline File object
   */
  @Test
  public void fileChannelPosition() throws Exception {
    RandomAccessFile file = new RandomAccessFile("temp/blahblah.txt", "r");
    file.seek(1000);

    FileChannel fileChannel = file.getChannel();
    assertEquals(1000, fileChannel.position());

    file.seek(500);
    assertEquals(500, fileChannel.position());

    fileChannel.position(200);
    assertEquals(200, file.getFilePointer());

    // clean up
    fileChannel.close();
    file.close();
  }

  /**
   * File lock is performed on PROCESS leve
   * @see FileLockTest
   */
  @Test
  public void fileLock() {
  }

  /**
   * @see MappedByteBufferBasedHttp, MappedByteBufferModes
   */
  @Test
  public void memoryMapFile() {
  }

  /**
   * @see FileChannelTransfer
   */
  @Test
  public void channelTransfer() {
  }

  /**
   * @see ExampleGreetingServerScoketChannel
   * @see ExampleGreetingSocketChannel
   * @see ExampleDatagramChannelServer
   * @see ExampleDatagramChannelClient
   */
  @Test
  public void socketChannel() {
  }

  /**
   * @see ExamplePipeInJVM
   */
  @Test
  public void pipe() {
  }

  /**
   * Stream, Reader/Writer with Channnel using Channels
   * @see Channels
   */
  @Test
  public void channelUtils() throws Exception {
    // populate channel
    ReadableByteChannel inChannel = Channels.newChannel(System.in);
    WritableByteChannel outChannel = Channels.newChannel(System.out);

    // populate stream
    InputStream in = Channels.newInputStream(inChannel);
    OutputStream out = Channels.newOutputStream(outChannel);

    // polulate reader/writer
    Reader reader = Channels.newReader(inChannel, "US-ASCII");
    Writer writer = Channels.newWriter(outChannel, "US-ASCII");

    // clean up
    reader.close();
    writer.close();
    in.close();
    out.close();
    inChannel.close();
    outChannel.close();
  }

  @Deprecated
  @Test
  public void navie() {
    assertTrue(true);
  }
}
