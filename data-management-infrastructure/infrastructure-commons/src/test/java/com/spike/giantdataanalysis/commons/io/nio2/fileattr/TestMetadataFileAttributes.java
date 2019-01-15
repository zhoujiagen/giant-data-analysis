package com.spike.giantdataanalysis.commons.io.nio2.fileattr;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import org.junit.Test;

/**
 * Metadata File Attributes
 * @author zhoujiagen
 */
public class TestMetadataFileAttributes {

  @Test
  public void supportedViews() {
    // output: basic owner user unix dos posix
    Set<String> views = FileSystems.getDefault().supportedFileAttributeViews();
    for (String view : views) {
      System.out.print(view + " ");
    }
    System.out.println();

    FileSystem fs = FileSystems.getDefault();
    for (FileStore store : fs.getFileStores()) {
      System.out.println(
        store.name() + ": " + store.supportsFileAttributeView(BasicFileAttributeView.class));
      System.out.println(store.name() + ": " + store.supportsFileAttributeView("basic"));
      System.out.println(
        store.name() + ": " + store.supportsFileAttributeView(AclFileAttributeView.class));
      System.out.println(store.name() + ": " + store.supportsFileAttributeView("acl"));
    }
  }

  @Test
  public void basiFileAttributeView() throws Exception {
    // a.txt is in $HOME
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    // get all attributes
    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
    System.out.println(attr.size());
    System.out.println(attr.creationTime());
    System.out.println(attr.lastAccessTime());
    System.out.println(attr.lastModifiedTime());

    System.out.println(attr.isDirectory());
    System.out.println(attr.isRegularFile());
    System.out.println(attr.isSymbolicLink());
    System.out.println(attr.isOther());

    // get a single attribute
    long size = (long) Files.getAttribute(path, "basic:size", LinkOption.NOFOLLOW_LINKS);
    System.out.println(size);

    // update attribute
    long time = System.currentTimeMillis();
    FileTime fileTime = FileTime.fromMillis(time);
    Files.getFileAttributeView(path, BasicFileAttributeView.class).setTimes(fileTime, fileTime,
      fileTime);
    Files.setLastModifiedTime(path, FileTime.fromMillis(time + 2000));
  }

  @Test
  public void dosFileAttributesView() throws Exception {
    // a.txt is in $HOME
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    // get all attributes
    DosFileAttributes attr = Files.readAttributes(path, DosFileAttributes.class);
    System.out.println(attr.isReadOnly());
    System.out.println(attr.isHidden());
    System.out.println(attr.isArchive());
    System.out.println(attr.isSystem());
  }

  @Test
  public void posixFileAttributeView() throws Exception {
    // a.txt is in $HOME
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    // get all attributes
    PosixFileAttributes attr = Files.readAttributes(path, PosixFileAttributes.class);

    System.out.println(attr.owner().getName());
    System.out.println(attr.group().getName());
    System.out.println(attr.permissions().toString());

    // set permissions
    Path new_path = Paths.get(System.getProperty("user.home"), "b.txt");
    // as a.txt
    FileAttribute<Set<PosixFilePermission>> posixattrs =
        PosixFilePermissions.asFileAttribute(attr.permissions());
    Files.createFile(new_path, posixattrs);
    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
    Files.setPosixFilePermissions(new_path, permissions);
  }

  @Test
  public void fileOwnerAttributeView() throws Exception {
    // a.txt is in $HOME

    // get
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");
    FileOwnerAttributeView foav = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
    System.out.println(foav.getOwner().getName());

    UserPrincipal owner =
        (UserPrincipal) Files.getAttribute(path, "owner:owner", LinkOption.NOFOLLOW_LINKS);
    System.out.println(owner.getName());

    // set
    UserPrincipal user =
        path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("zhoujiagen");
    Files.setOwner(path, user);
    // read again
    System.out.println(Files.getOwner(path, LinkOption.NOFOLLOW_LINKS).getName());
  }

  @Test
  public void aclFileAttributeView() {
    // unsupported in Ubuntu 14.04
    FileSystem fs = FileSystems.getDefault();
    for (FileStore store : fs.getFileStores()) {
      System.out.println(
        store.name() + ": " + store.supportsFileAttributeView(AclFileAttributeView.class));
      System.out.println(store.name() + ": " + store.supportsFileAttributeView("acl"));
    }
  }

  @Test
  public void fileStoreAttributeView() throws Exception {
    // a.txt is in $HOME
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");
    FileStore fs = Files.getFileStore(path);

    int factorOfMB = 1024 * 1024;
    System.out.println(fs.isReadOnly());
    System.out.println(fs.getTotalSpace() / factorOfMB);
    System.out.println(fs.getUsableSpace() / factorOfMB);
    System.out.println(fs.getUnallocatedSpace() / factorOfMB);
  }

  @Test
  public void userDefinedFileAttributeView() throws Exception {
    // a.txt is in $HOME
    Path path = Paths.get(System.getProperty("user.home"), "a.txt");

    // define
    UserDefinedFileAttributeView udfav =
        Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
    String userAttrName = "file.description";
    int written = udfav.write(userAttrName,
      Charset.defaultCharset().encode("This file contains private information"));
    System.out.println(written);

    // list
    int size = udfav.size(userAttrName);
    ByteBuffer bb = ByteBuffer.allocate(size);
    udfav.read(userAttrName, bb);
    bb.flip();
    System.out.println(Charset.defaultCharset().decode(bb).toString());

    // delete
    udfav.delete(userAttrName);
  }
}
