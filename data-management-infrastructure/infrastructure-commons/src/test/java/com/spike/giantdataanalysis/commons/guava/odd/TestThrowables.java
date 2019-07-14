package com.spike.giantdataanalysis.commons.guava.odd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

/**
 * <pre>
 * {@link Throwables}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestThrowables {

  @Test
  public void demo() {

    List<Throwable> throwables = null;

    try {

      File file = new File("non-existed-file.txt");
      ByteSource byteSource = Files.asByteSource(file);
      byteSource.copyTo(System.out);

    } catch (IOException e) {
      // e.printStackTrace();
      throwables = Throwables.getCausalChain(e);
      this.usingThrowables(e);
    }

    System.out.println(throwables.size());
    System.out.println(throwables);

  }

  private void usingThrowables(Exception e) {
    List<StackTraceElement> elements = Throwables.lazyStackTrace(e);
    // System.out.println(elements);
    for (StackTraceElement element : elements) {
      if (this.interestingClass(element, "com.spike.foundation")) {
        System.err.println(element);
      }
    }
  }

  private boolean interestingClass(StackTraceElement element, String description) {
    return element.getClassName().contains(description);
  }

}
