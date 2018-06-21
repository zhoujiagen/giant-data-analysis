package com.spike.giantdataanalysis.commons.openjdk;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public final class OpenJDKJOLs {

  private OpenJDKJOLs() {
  }

  public static String vmDetails() {
    return VM.current().details();
  }

  public static String classLayout(Class<?> clazz) {
    return ClassLayout.parseClass(clazz).toPrintable();
  }
}
