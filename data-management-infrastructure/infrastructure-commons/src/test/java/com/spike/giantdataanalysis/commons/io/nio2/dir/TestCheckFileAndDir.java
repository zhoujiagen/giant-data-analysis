package com.spike.giantdataanalysis.commons.io.nio2.dir;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Checking method for files and directories
 * @author zhoujiagen
 */
public class TestCheckFileAndDir {

  @Test
  public void exist() {
    Path path1 = Paths.get(System.getProperty("user.home"), "a.txt");
    System.out.println(Files.exists(path1));
    Path path2 = Paths.get(System.getProperty("user.home"), "nonexist.txt");
    System.out.println(Files.notExists(path2));
  }

  @Test
  public void accessbility() throws Exception {
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");
    System.out.println(Files.isReadable(path));
    System.out.println(Files.isWritable(path));
    System.out.println(Files.isExecutable(path));
    System.out.println(Files.isRegularFile(path));
    System.out.println(Files.isHidden(path));
  }

  @Test
  public void isSame() throws Exception {
    Path path1 = Paths.get(System.getProperty("user.home"), "a.txt");
    Path path2 = Paths.get("/home/zhoujiagen", "a.txt");

    assertTrue(Files.isSameFile(path1, path2));
  }
}
