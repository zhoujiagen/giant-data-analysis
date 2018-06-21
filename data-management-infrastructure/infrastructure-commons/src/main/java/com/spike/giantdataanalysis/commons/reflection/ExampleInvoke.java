package com.spike.giantdataanalysis.commons.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import com.spike.giantdataanalysis.commons.annotation.constraint.runtime.Example;

/**
 * @see java.lang.invoke.MethodType
 * @see java.lang.invoke.MethodHandle
 * @see java.lang.invoke.MethodHandles
 */
@Example(description = "java.lang.invoke示例")
public class ExampleInvoke {

  public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
    // 面向对象的方式使用反射的方法
    System.out.println(MethodType.methodType(String.class)); // ()String
    System.out.println(MethodType.methodType(int.class, String.class)); // (String)int

    // 方法描述
    // ClassLoader
    // Class<?> loadClass(String name) throws ClassNotFoundException
    MethodType loadClass = MethodType.methodType(Class.class, String.class);
    System.out.println("MethodType: " + loadClass);
    // 方法句柄
    Lookup lookup = MethodHandles.lookup();
    MethodHandle methodHandle = lookup.findVirtual(ClassLoader.class, "loadClass", loadClass);
    System.out.println("MethodHandle: " + methodHandle);

    // 调用方法句柄
    // Object
    // public native int hashCode();
    Object object = "hello";
    MethodType hashCode = MethodType.methodType(int.class);
    MethodHandle hashCodeMethodHandle = lookup.findVirtual(Object.class, "hashCode", hashCode);
    try {
      int hashCodeValue = (int) hashCodeMethodHandle.invoke(object);
      System.out.println(hashCodeValue);
    } catch (Throwable e) {
      e.printStackTrace();
    }

  }
}
