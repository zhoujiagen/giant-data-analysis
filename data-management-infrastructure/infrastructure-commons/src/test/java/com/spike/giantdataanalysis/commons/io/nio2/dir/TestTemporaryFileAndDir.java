package com.spike.giantdataanalysis.commons.io.nio2.dir;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Temporary files and directories
 * @author zhoujiagen
 */
public class TestTemporaryFileAndDir {

  @Test
  public void tempDir() throws Exception {
    System.out.println(System.getProperty("java.io.tmpdir"));

    String prefix = "nio2_";
    Path base = Paths.get(System.getProperty("java.io.tmpdir"));
    Path path = Files.createTempDirectory(base, prefix);
    System.out.println(path);
  }

  @Test
  public void shutdownHook() throws Exception {
    String prefix = "nio2_";
    Path base = Paths.get(System.getProperty("java.io.tmpdir"));
    final Path path = Files.createTempDirectory(base, prefix);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.out.println("Deleting tmp folder");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
          for (Path file : ds) {
            Files.delete(file);
          }
          Files.delete(path);
        } catch (Exception e) {
          e.printStackTrace();
        }

        System.out.println("Deleting tmp folder done");
      }
    });
  }

  @Test
  public void tempDirDeleteOnExit() throws Exception {
    String prefix = "nio2_";
    Path base = Paths.get(System.getProperty("java.io.tmpdir"));
    Path path = Files.createTempDirectory(base, prefix);

    File file = path.toFile();// convert to regular File
    file.deleteOnExit();
  }

  @Test
  public void tempFile() throws Exception {
    String prefix = "nio2_";
    String suffix = ".txt";
    Path base = Paths.get(System.getProperty("java.io.tmpdir"));
    Path path = Files.createTempFile(base, prefix, suffix);
    System.out.println(path);
  }

}
