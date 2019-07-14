package com.spike.giantdataanalysis.commons.io.nio2.raf;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import org.junit.Test;

import com.spike.giantdataanalysis.commons.io.util.Environments;

/**
 * Random Access File:<br/>
 * @author zhoujiagen
 * @see SeekableByteChannel
 * @see WritableByteChannel
 * @see ReadableByteChannel
 * @see FileChannel
 */
public class TestRandomAccessFile {

  @Test
  public void seekableByteChannel() {
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    // write
    try (SeekableByteChannel channel = Files.newByteChannel(path,
      EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {

      ByteBuffer bb =
          ByteBuffer.wrap("what is rational is real, and what is real is rational.".getBytes());
      int bytes = channel.write(bb);
      System.out.println(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // read
    try (SeekableByteChannel channel =
        Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ))) {
      ByteBuffer bb = ByteBuffer.allocate(20);
      String charset = System.getProperty("file.encoding");

      while (channel.read(bb) > 0) {
        bb.flip();
        System.out.print(Charset.forName(charset).decode(bb));
        bb.clear();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void positionOfSeekableByteChannel() {
    // prepare data
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");
    String firstLine = "what is rational is real, and what is real is rational.";
    try (SeekableByteChannel channel = Files.newByteChannel(path,
      EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
      StringBuffer sb = new StringBuffer();
      sb.append(firstLine);
      sb.append(Environments.NEWLINE);
      sb.append("Hello there");
      ByteBuffer bb = ByteBuffer.wrap(sb.toString().getBytes());
      int bytes = channel.write(bb);
      System.out.println(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // copy first line to the end of file
    ByteBuffer bb = ByteBuffer.allocate(firstLine.length() + 1);
    bb.put(Environments.NEWLINE.getBytes());
    try (SeekableByteChannel channel =
        Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.READ))) {

      int bytes = 0;
      do {
        bytes = channel.read(bb);
      } while (bytes != -1 && bb.hasRemaining());

      bb.flip();

      channel.position(channel.size());// seek to the end of file

      while (bb.hasRemaining()) {
        channel.write(bb);
      }

      bb.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @see ChannelTest#fileChannelPosition()
   * @see ChannelTest#channelTransfer()
   * @see FileLockTest
   */
  @Test
  public void fileChannel() {
  }

}
