package com.spike.giantdataanalysis.commons.reflection;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import com.spike.giantdataanalysis.commons.reflection.classloader.RawClassLoader;

public class TestRawClassLoader {

  public static void main(String[] args) throws ClassNotFoundException, IOException {

    URL[] urls =
        new URL[] { new URL("file:///target/infrastructure-benchmark-0.0.1-SNAPSHOT.jar") };
    RawClassLoader loader = new RawClassLoader(urls);
    Class<?> clazz =
        loader.loadClass("com.spike.giantdataanalysis.benchmark.support.timing.TimingUtil");

    for (Method method : clazz.getDeclaredMethods()) {
      System.out.println(method);
    }

    loader.close();
  }
}
