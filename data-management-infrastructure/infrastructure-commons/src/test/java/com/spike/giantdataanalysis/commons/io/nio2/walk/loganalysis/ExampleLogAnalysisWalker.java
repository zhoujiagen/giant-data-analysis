package com.spike.giantdataanalysis.commons.io.nio2.walk.loganalysis;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Log Analysis Walker
 * @author zhoujiagen
 * @see Walker
 */
public class ExampleLogAnalysisWalker extends SimpleFileVisitor<Path> {
  // prefix of log file name
  private static final String FILE_PREFIX = "prefix";
  // suffix of log file name
  private static final String FILE_SUFFIX = ".suffix";

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    System.out.println("\t\\--" + file.getFileName().toString());

    // do something here
    try {
      String fileName = file.getFileName().toString();
      // remove analysis result files
      if (fileName.startsWith(FILE_PREFIX) && fileName.endsWith(FILE_SUFFIX)) {
        // TODO: introduce a thread poll
        new ExampleLogAnalysisTask(file).call();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    System.out
        .println("\n" + dir.getParent() + System.getProperty("file.separator") + dir.getFileName());

    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    if (exc != null) exc.printStackTrace();
    return FileVisitResult.CONTINUE;
  }
}
