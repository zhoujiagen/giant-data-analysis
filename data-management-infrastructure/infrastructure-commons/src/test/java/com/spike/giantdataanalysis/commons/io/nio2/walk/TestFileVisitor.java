package com.spike.giantdataanalysis.commons.io.nio2.walk;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class TestFileVisitor {
  @Test
  public void testWalkDirRecurisive() throws IOException {
    // 读文件系统中路径
    Path start = FileSystems.getDefault().getPath("C:/logs", "20150414");
    FileVisitor<? super Path> visitor = new MyDirWalker();
    Files.walkFileTree(start, visitor);
  }

  /**
   * Directory Walker <br>
   * @author zhoujiagen
   */
  public class MyDirWalker extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      System.out.println("\t\\--" + file.getFileName().toString());

      // do something here

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {
      System.out.println(
        "\n" + dir.getParent() + System.getProperty("file.separator") + dir.getFileName());

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      if (exc != null) exc.printStackTrace();
      return FileVisitResult.CONTINUE;
    }
  }

}
