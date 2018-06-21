package com.spike.giantdataanalysis.commons.reflection.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @see java.lang.ClassLoader
 */
public class DiskClassLoader extends ClassLoader {

  public DiskClassLoader() {
    super(DiskClassLoader.class.getClassLoader());
  }

  public DiskClassLoader(ClassLoader parent) {
    super(parent);
  }

  /**
   * 从硬盘加载类. <code>
   * valid name:
   * "java.lang.String"
   * "javax.swing.JSpinner$DefaultEditor"
   * "java.security.KeyStore$Builder$FileBuilder$1"
   * "java.net.URLClassLoader$3$1"
   * </code>
   * @param path .class文件路径
   * @param name 类的二进制名称
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Class<?> loadClassFromDisk(String path, String name)
      throws IOException, ClassNotFoundException {
    byte[] classBytes = Files.readAllBytes(Paths.get(path));
    return defineClass(name, classBytes, 0, classBytes.length);
  }

}
