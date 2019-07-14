package com.spike.giantdataanalysis.commons.io.nio2.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestPath {

  @Test
  public void path() {
    String dir = "C:/sample";
    String file = "sample.txt";
    Path path = Paths.get(dir, file);
    assertEquals(path.getFileName().toString(), file);
  }

  @Test
  public void normalize() {
    Path path1 = Paths.get("temp/../temp/BNP.txt");
    assertEquals("temp/../temp/BNP.txt", path1.toString());

    Path path2 = Paths.get("temp/../temp/BNP.txt").normalize();
    assertEquals("temp/BNP.txt", path2.toString());
  }

  /**
   * @see URI
   */
  @Test
  public void pathFromURI() {
    Path path = Paths.get(URI.create("file:///temp/BNP.txt"));
    System.out.println(path);
  }

  /**
   * @see FileSystems
   */
  @Test
  public void pathFromFileSystem() {
    Path path = FileSystems.getDefault().getPath("temp/BNP.txt");
    System.out.println(path);
  }

  @Test
  public void pathFromOSHome() {
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");
    System.out.println(path);
  }

  @Test
  public void informationOfPath() {
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    System.out.println(path.getFileName());
    System.out.println(path.getRoot());
    System.out.println(path.getParent());
    System.out.println(path.getNameCount());
    for (int i = 0; i < path.getNameCount(); i++) {
      System.out.print(path.getName(i) + " ");
    }
    System.out.println();

    System.out.println(path.subpath(0, path.getNameCount() - 1));

  }

  @Test
  public void convert() throws Exception {
    Path path = Paths.get("/a/b/c", "BNP.txt");
    System.out.println(path.toString());

    URI uri = path.toUri();
    System.out.println(uri);

    Path absolutePath = path.toAbsolutePath();
    System.out.println(absolutePath);

    // a link file: alink.txt
    Path alink = Paths.get(System.getProperty("user.home"), "alink.txt");
    System.out.println(alink.toRealPath(LinkOption.NOFOLLOW_LINKS));
    System.out.println(alink.toRealPath());

    File file = path.toFile();
    System.out.println(file);
    System.out.println(file.toPath());
  }

  @Test
  public void resolve() {
    Path base = Paths.get("/A/B");
    Path bnp = base.resolve("BNP.txt");
    System.out.println(bnp);

    Path agent = bnp.resolveSibling("AGENT.txt");
    System.out.println(agent);
  }

  @Test
  public void relative() {
    Path path1 = Paths.get("/A/B/C/BNP.txt");
    Path path2 = Paths.get("/A/B/D");

    System.out.println(path1.relativize(path2));
    System.out.println(path2.relativize(path1));
  }

  @Test
  public void compare() throws Exception {
    Path path1 = Paths.get("/tmp/A/B/C/BNP.txt");
    Path path2 = Paths.get("/tmp/A/B/D/../C/BNP.txt");

    assertFalse(path1.equals(path2));
    // should locate in real OS
    assertTrue(Files.isSameFile(path1, path2));
  }

  @Test
  public void elementsOfPath() {
    Path path = Paths.get("/tmp/A/B/C/BNP.txt");
    for (Path p : path) {
      System.out.println(p);
    }
  }
}
