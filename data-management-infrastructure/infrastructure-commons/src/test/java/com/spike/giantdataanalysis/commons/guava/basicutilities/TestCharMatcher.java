package com.spike.giantdataanalysis.commons.guava.basicutilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.CharMatcher;

/**
 * <pre>
 * {@link CharMatcher}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestCharMatcher {

  String sentenseWithLineSeparator =
      "What's your point?" + System.lineSeparator() + "Guava is more.";

  /**
   * <pre>
   * 换行符替换为空格符
   * </pre>
   */
  @Test
  public void replaceLineSeparatorWithBlanSpace() {
    String result = CharMatcher.BREAKING_WHITESPACE.replaceFrom(sentenseWithLineSeparator, ' ');

    assertEquals("What's your point? Guava is more.", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * 合并多个连续的空格或TAB
   * </pre>
   */
  @Test
  public void collapse() {
    String sentense = "          what           's your  point     ?";

    String result = CharMatcher.WHITESPACE.collapseFrom(sentense, ' ');
    assertEquals(" what 's your point ?", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * trim 和 合并多个连续的空格或TAB
   * </pre>
   */
  @Test
  public void trimAndCollapse() {
    String sentense = "          what           's your  point     ?";

    String result = CharMatcher.WHITESPACE.trimAndCollapseFrom(sentense, ' ');
    assertEquals("what 's your point ?", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * 保留字符串中的数字
   * </pre>
   */
  @Test
  public void retainFrom() {
    String letterAndNumber = "foo989yxbar234";
    String result = CharMatcher.JAVA_DIGIT.retainFrom(letterAndNumber);

    assertEquals("989234", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * CharMatcher组合
   * </pre>
   */
  @Test
  public void or() {
    String letterAndNumber = "foo989 yxbar234 ";

    // 数字或者空格
    CharMatcher cm = CharMatcher.JAVA_DIGIT.or(CharMatcher.WHITESPACE);
    String result = cm.retainFrom(letterAndNumber);

    assertEquals("989 234 ", result);
    System.out.println(result);
  }

}
