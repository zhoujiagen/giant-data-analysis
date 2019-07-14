package com.spike.giantdataanalysis.commons.guava.basicutilities;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

/**
 * <pre>
 * Guava中字符串的单元测试
 * </pre>
 *
 * @see com.google.common.base.Charsets
 * @see com.google.common.base.Strings
 * @author zhoujiagen
 */
public class TestString {

  @Test
  public void charsets() {
    String content = "what's the point?";

    byte[] bytes = content.getBytes(Charsets.UTF_8);

    System.out.println(new String(bytes, Charsets.UTF_8));
  }

  /**
   * <pre>
   * </pre>
   *
   * @see Strings#emptyToNull(String)
   * @see Strings#isNullOrEmpty(String)
   * @see Strings#nullToEmpty(String)
   */
  @Test
  public void strings() {
    // 在尾部补全
    String result = Strings.padEnd("foo", 10, ' ');
    System.out.println("|" + result + "|");

    // 在头部补全
    result = Strings.padStart("foo", 10, ' ');
    System.out.println("|" + result + "|");
  }
}
