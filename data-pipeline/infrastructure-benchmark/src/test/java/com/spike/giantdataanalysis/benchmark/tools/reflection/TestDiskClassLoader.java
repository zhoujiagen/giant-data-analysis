package com.spike.giantdataanalysis.benchmark.tools.reflection;

import java.io.IOException;
import java.lang.reflect.Method;

import com.spike.giantdataanalysis.benchmark.tools.reflection.classloader.DiskClassLoader;

public class TestDiskClassLoader {

  public static void main(String[] args) throws ClassNotFoundException, IOException {
    DiskClassLoader loader = new DiskClassLoader();

    String path =
        "target/classes/com/spike/giantdataanalysis/benchmark/support/timing/TimingUtil.class";
    Class<?> clazz = loader.loadClassFromDisk(path,
      "com.spike.giantdataanalysis.benchmark.support.timing.TimingUtil");

    for (Method method : clazz.getDeclaredMethods()) {
      System.out.println(method);
    }
  }
}
