package com.spike.giantdataanalysis.commons.lang;

import static com.spike.giantdataanalysis.commons.lang.StringUtils.FILE_SEP;
import static com.spike.giantdataanalysis.commons.lang.StringUtils.REPEAT;
import static com.spike.giantdataanalysis.commons.lang.StringUtils.TAB;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;

/**
 * <pre>
 * 文件操作工具类
 * </pre>
 *
 * @author zhoujiagen
 */
public final class FileUtils {

  /**
   * mock system <code>tree</code> command
   * @param dirPath
   */
  public static final void tree(String dirPath) {
    if (!Files.isDirectory(Paths.get(dirPath), LinkOption.NOFOLLOW_LINKS)) {
      return;
    }

    FileVisitor<? super Path> visitor = new InnerFileVisitor(dirPath);
    try {
      Files.walkFileTree(Paths.get(dirPath), visitor);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * get all sub-directories
   * @param dirPath The directory path
   * @return ordered (short) sub-directory name tree set
   */
  public static final TreeSet<String> subPaths(final String dirPath) {
    if (!Files.isDirectory(Paths.get(dirPath), LinkOption.NOFOLLOW_LINKS)) {
      return null;
    }
    final TreeSet<String> result = new TreeSet<String>();

    try {
      Files.walkFileTree(Paths.get(dirPath), new FileVisitor<Path>() {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
          if (!Paths.get(dirPath).equals(dir)) {
            String thisPath = dir.toAbsolutePath().toString();
            thisPath = thisPath.substring(dirPath.length() + 1, thisPath.length());

            result.add(thisPath);
          }

          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
          if (exc != null) exc.printStackTrace();
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          if (exc != null) exc.printStackTrace();
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  private static class InnerFileVisitor extends SimpleFileVisitor<Path> {

    private int level;

    public InnerFileVisitor(String pathPrefix) {
      this.level = pathPrefix.split(FILE_SEP).length;
      System.out.println(level);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      System.out
          .println(REPEAT(TAB, file.toAbsolutePath().toString().split(FILE_SEP).length - level)
              + file.getFileName().toString());

      // do something here

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {

      System.out.println(REPEAT(TAB, dir.toAbsolutePath().toString().split(FILE_SEP).length - level)
          + dir.getFileName());

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      if (exc != null) exc.printStackTrace();
      return FileVisitResult.CONTINUE;
    }
  }

}
