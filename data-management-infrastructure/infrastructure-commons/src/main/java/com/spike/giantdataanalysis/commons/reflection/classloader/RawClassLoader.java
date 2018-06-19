package com.spike.giantdataanalysis.commons.reflection.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @see java.net.URLClassLoader
 * @see java.net.URLStreamHandler
 */
public class RawClassLoader extends URLClassLoader {

  public RawClassLoader(URL[] urls) {
    super(urls, RawClassLoader.class.getClassLoader());
  }

  public RawClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  /**
   * @param urls
   * @param parent
   * @param factory jar, file, http, https, ftp etc protocol stream handler factory
   */
  public RawClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
    super(urls, parent, factory);
  }
}
