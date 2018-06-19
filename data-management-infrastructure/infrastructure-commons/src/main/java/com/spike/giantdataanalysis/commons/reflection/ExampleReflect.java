package com.spike.giantdataanalysis.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.spike.giantdataanalysis.commons.annotation.constraint.runtime.Example;
import com.spike.giantdataanalysis.commons.annotation.constraint.runtime.Parameter.*;;

@Example(description = "java.lang.reflect包示例")
public class ExampleReflect {

  public static class MyCahe {

    @Example(description = "flush on some condition")
    private void flush(@IN String content, @OUT String rtn) throws Exception {
      System.out.println("flushing...");
    }
  }

  public static void main(String[] args) {

    Class<?> clazz = MyCahe.class;
    try {
      // object
      Object instance = clazz.newInstance();

      // method
      Method methodFlush =
          clazz.getDeclaredMethod("flush", new Class<?>[] { String.class, String.class });
      methodFlush.setAccessible(true);
      System.out.println("Method name: " + methodFlush.getName());
      methodFlush.invoke(instance, "hello", "");

      // annotation on method
      // Well, the annotation RetentionPolicy should be RUNTIME
      for (Annotation anno : methodFlush.getAnnotations()) {
        System.out.println("Annotation: " + anno);
      }
      for (Example annoInstance : methodFlush.getDeclaredAnnotationsByType(Example.class)) {
        System.out.println("Annotation value: " + annoInstance.description());
      }

      // exception on method
      for (Class<?> exType : methodFlush.getExceptionTypes()) {
        System.out.println("Exception Type: " + exType);
      }

      // parameter and return types
      for (Class<?> paramType : methodFlush.getParameterTypes()) {
        System.out.println("Parameter Type: " + paramType);
      }
      for (Annotation[] paramAnnos : methodFlush.getParameterAnnotations()) {
        for (Annotation paramAnno : paramAnnos) {
          System.out.println("Parameter Annotation: " + paramAnno);
        }
      }
      System.out.println("Return Type: " + methodFlush.getReturnType());

    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

  }
}
