package com.spike.giantdataanalysis.commons.guava.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;

/**
 * <pre>
 * Guava {@link Files}等文件处理的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
@SuppressWarnings("deprecation")
public class TestFiles {

  private String prefix = "src/main/resources/files/";

  /**
   * <pre>
   * 文件拷贝
   * </pre>
   * 
   * @throws IOException
   * @see {@link Files#asByteSource(File)}
   * @see Files#asByteSink(File, com.google.common.io.FileWriteMode...)
   */
  @Test
  public void copy() throws IOException {
    File from = new File(prefix + "a.txt");
    File to = new File(prefix + "b.txt");

    to.deleteOnExit();

    Files.copy(from, to);
  }

  /**
   * <pre>
   * 移动或者重命名
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void move_or_renaming() throws IOException {
    File from = new File(prefix + "c.txt");
    File to = new File(prefix + "cc.txt");

    Files.move(from, to);

    Files.move(to, from);// 回置操作
  }

  /**
   * <pre>
   * 将小文件视为字符串列表
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void readFileAsString() throws IOException {
    File file = new File(prefix + "d.txt");

    List<String> lines = Files.readLines(file, Charsets.UTF_8);
    System.out.println(lines);

    file = new File(prefix + "book.txt");

    // 使用行处理器
    LineProcessor<List<String>> callback = new LineProcessor<List<String>>() {
      private Splitter splitter = Splitter.on(",");
      private List<String> bookTitles = Lists.<String> newArrayList();
      private static final int TITLE_INDEX = 2;

      @Override
      public boolean processLine(String line) throws IOException {
        // 添加记录校验等
        bookTitles.add(Iterables.get(splitter.split(line), TITLE_INDEX));
        return true;
      }

      @Override
      public List<String> getResult() {
        return bookTitles;
      }
    };
    lines = Files.readLines(file, Charsets.UTF_8, callback);
    System.out.println(lines);
  }

  /**
   * <pre>
   * 文件的hash
   * </pre>
   * 
   * @throws IOException
   * @see Hashing
   */
  @Test
  public void hashFile() throws IOException {
    File file = new File(prefix + "book.txt");

    HashCode fileHashCode = Files.hash(file, Hashing.md5());
    System.out.println(fileHashCode);
  }

  @Test
  public void write_append_overwrite() throws IOException {
    File file = new File(prefix + "book2.txt");

    file.deleteOnExit();// JVM停止时删除

    // 写入
    Files.write("what do", file, Charsets.UTF_8);

    // 追加
    Files.append(" you want to do?", file, Charsets.UTF_8);

    // 覆盖
    Files.write("new content", file, Charsets.UTF_8);
  }

  /**
   * <pre>
   * Source和Sink抽象
   * </pre>
   * 
   * @throws IOException
   * @see ByteSource
   * @see ByteSink
   */
  @Test
  public void source_sink() throws IOException {

    // 1 source
    File file = new File(prefix + "book.txt");

    ByteSource byteSource = Files.asByteSource(file);
    // 比较字节数组
    Assert.assertThat(byteSource.read(), Is.is(Files.toByteArray(file)));

    // 2 sink
    file = new File(prefix + "Git-Cheat-Sheet.png");
    File target = new File(prefix + "Git-Cheat-Sheet[copy].png");

    target.deleteOnExit();

    ByteSink byteSink = Files.asByteSink(target);
    byteSink.write(Files.toByteArray(file));
  }

  /**
   * <pre>
   * 从Source拷贝到Sink
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void copyFromSourceToSink() throws IOException {
    File sourceFile = new File(prefix + "Git-Cheat-Sheet.png");
    File targetFile = new File(prefix + "Git-Cheat-Sheet[copy].png");
    targetFile.deleteOnExit();

    ByteSource source = Files.asByteSource(sourceFile);
    ByteSink sink = Files.asByteSink(targetFile);

    source.copyTo(sink);
  }

  /**
   * <pre>
   * {@link ByteStreams}
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void byteStreams() throws IOException {
    File sourceFile = new File(prefix + "Git-Cheat-Sheet.png");
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceFile));

    InputStream inputStream = ByteStreams.limit(in, 10);
    Assert.assertEquals(10, inputStream.available());
    System.out.println(in.available());

    in.close();
    inputStream.close();
  }

  /**
   * <pre>
   * {@link InputSupplier}已被废弃
   * 
   * 合并文件
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void charStreams() throws IOException {
    File aFile = new File(prefix + "a.txt");
    File cFile = new File(prefix + "c.txt");

    File joinedFile = new File(prefix + "joined.txt");

    joinedFile.deleteOnExit();

    ByteSource aByteSource = Files.asByteSource(aFile);
    ByteSource cByteSource = Files.asByteSource(cFile);

    ByteSink joinedSink = Files.asByteSink(joinedFile, FileWriteMode.APPEND);

    aByteSource.copyTo(joinedSink);
    cByteSource.copyTo(joinedSink);

  }

  /**
   * <pre>
   *  {@link Closer}实现
   *  
   *  模拟Java7 的try-with-resource
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void closer() throws IOException {

    Closer closer = Closer.create();

    try {
      File aFile = new File(prefix + "a.txt");

      BufferedReader reader = new BufferedReader(new FileReader(aFile));
      closer.register(reader);

      PrintStream out = System.out;
      closer.register(out);

      String line = null;
      while ((line = reader.readLine()) != null) {
        out.write(line.getBytes());
      }

    } catch (Exception e) {

      throw closer.rethrow(e);

    } finally {

      closer.close();
    }

  }

  @Test
  public void base64() throws IOException {
    File file = new File(prefix + "a.txt");
    byte[] fileBytes = Files.toByteArray(file);

    BaseEncoding base64 = BaseEncoding.base64();

    // encode
    String encodedContent = base64.encode(fileBytes);
    System.out.println(encodedContent);

    // decode
    byte[] decodecContent = base64.decode(encodedContent);
    Assert.assertThat(decodecContent, Is.is(fileBytes));
  }
}
