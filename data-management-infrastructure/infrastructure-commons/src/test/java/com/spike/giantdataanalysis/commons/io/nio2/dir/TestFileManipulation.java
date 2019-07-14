package com.spike.giantdataanalysis.commons.io.nio2.dir;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * create, read, and write files
 * @author zhoujiagen
 */
public class TestFileManipulation {
  private static final String DEFAULT_CHARSET = "UTF-8";

  @Test
  public void create() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");

    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-------");
    FileAttribute<Set<PosixFilePermission>> attrs =
        PosixFilePermissions.asFileAttribute(permissions);
    Files.createFile(path, attrs);

    assertTrue(Files.exists(path));
  }

  @Test
  public void writeSmallFileUsingBytes() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");

    String content = "what is rational is real, and what is real is rational.";
    Files.write(path, content.getBytes(DEFAULT_CHARSET));
  }

  @Test
  public void writeSmallFileUsingLines() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");

    List<String> lines = new ArrayList<String>();
    lines.add("1 what is rational is real, and what is real is rational.");
    lines.add("2 what is rational is real, and what is real is rational.");
    lines.add("3 what is rational is real, and what is real is rational.");

    Files.write(path, lines, Charset.forName(DEFAULT_CHARSET), StandardOpenOption.APPEND);
  }

  @Test
  public void readSmallFileUsingByte() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");
    byte[] bytes = Files.readAllBytes(path);

    System.out.println(new String(bytes, Charset.forName(DEFAULT_CHARSET)));
  }

  @Test
  public void readSmallFileUsingLines() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");
    List<String> allLines = Files.readAllLines(path, Charset.forName(DEFAULT_CHARSET));
    for (String line : allLines) {
      System.out.println(line);
    }
  }

  @Test
  public void writerAndReader() {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");

    String content = "Ah ha!";
    try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName(DEFAULT_CHARSET),
      StandardOpenOption.APPEND)) {
      writer.write(content);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName(DEFAULT_CHARSET))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void inOutStream() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "newfile.txt");

    String content = "Ah ha! Stream!";
    try (OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.APPEND);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      writer.write(content);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try (InputStream inputStream = Files.newInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void copy() throws Exception {
    Path from = Paths.get(System.getProperty("user.home"), "a.txt");
    Path to = Paths.get(System.getProperty("user.home"), "newa.txt");

    Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES,
      LinkOption.NOFOLLOW_LINKS);
  }

  @Test
  public void move() throws Exception {
    Path from = Paths.get(System.getProperty("java.io.tmpdir"), "nio2.txt");
    Path to = Paths.get(System.getProperty("user.home"), "nio2.txt");

    Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
  }

  @Test
  public void renameUsingMove() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "nio2.txt");

    Files.move(path, path.resolveSibling("nio2_renamed.txt"), StandardCopyOption.REPLACE_EXISTING);
  }
}
