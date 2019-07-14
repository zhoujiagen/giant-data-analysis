package com.spike.giantdataanalysis.commons.io.nio2.link;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

/**
 * Symbolic and Hard Link
 * @author zhoujiagen
 */
public class TestSymbolicAndHardLink {

  @Test
  public void symbolicLink() throws Exception {
    Path link = FileSystems.getDefault().getPath("temp/symbolicLinktoatxt");
    Path target = FileSystems.getDefault().getPath(System.getProperty("user.home"), "a.txt");

    System.out.println(link.toAbsolutePath());
    Files.createSymbolicLink(link, target);
    System.out.println(Files.isSymbolicLink(link));

    // locate target of a link
    Path linkTargetPath = Files.readSymbolicLink(link);
    System.out.println(linkTargetPath);

    // check is same file
    System.out.println(Files.isSameFile(link, target));
  }

  @Test
  public void hardLink() throws Exception {
    Path link = FileSystems.getDefault().getPath("temp/hardLinktoatxt");
    Path target = FileSystems.getDefault().getPath(System.getProperty("user.home"), "a.txt");

    System.out.println(link.toAbsolutePath());
    Files.createLink(link, target);
  }

}
