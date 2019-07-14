package com.spike.giantdataanalysis.commons.guava.basicutilities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

/**
 * <pre>
 * {@link Joiner}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestJoiner {

  private List<String> words;

  @Before
  public void setUp() {
    words = new ArrayList<String>();

    words.add("word1");
    words.add("word2");
    words.add(null);
    words.add("word4");
  }

  @Test
  public void join() {
    // 跳过空值
    String result = Joiner.on("|").skipNulls().join(words);
    assertEquals("word1|word2|word4", result);
    System.out.println(result);

    // 用默认值替代空值
    result = Joiner.on("|").useForNull("<NULL>").join(words);
    assertEquals("word1|word2|<NULL>|word4", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * </pre>
   *
   * @see {@link java.lang.Appendable}
   */
  @Test
  public void append() {

    StringBuilder sb = new StringBuilder();

    // 实例化Joiner
    Joiner joiner = Joiner.on("|").skipNulls();

    // 追加到Appendable实例上
    joiner.appendTo(sb, words);

    assertEquals("word1|word2|word4", sb.toString());
    System.out.println(sb.toString());
  }

  /**
   * <pre>
   * </pre>
   * 
   * @see com.google.common.collect.Maps
   */
  @Test
  public void mapJoin() {

    // 链接的Map实现
    Map<String, String> map = com.google.common.collect.Maps.newLinkedHashMap();
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.put("key3", "value3");

    String lineSep = System.lineSeparator();
    Joiner.MapJoiner mapJoiner = Joiner.on(lineSep).withKeyValueSeparator("=");
    String result = mapJoiner.join(map);

    assertEquals("key1=value1" + lineSep + "key2=value2" + lineSep + "key3=value3", result);
    System.out.println(result);
  }

}
