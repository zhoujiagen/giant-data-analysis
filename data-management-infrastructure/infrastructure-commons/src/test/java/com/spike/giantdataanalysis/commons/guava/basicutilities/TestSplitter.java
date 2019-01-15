package com.spike.giantdataanalysis.commons.guava.basicutilities;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Splitter;

/**
 * <pre>
 * {@link Splitter}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestSplitter {

  private String sentense = "           Monday,Tuesday,,Thursday,Friday,,           ";

  @Test
  public void split() {
    Iterable<String> result = Splitter.on("|").split("foo|bar|baz");
    System.out.println(result);

    result = Splitter.on(",").split(sentense);
    System.out.println(result);
  }

  @Test
  public void trimResult() {
    // 移除结果中的空格
    Splitter splitter = Splitter.on(",").trimResults();

    Iterable<String> result = splitter.split(sentense);
    System.out.println(result);
  }

  @Test
  public void mapSplitter() {
    String lineSep = System.lineSeparator();
    Splitter.MapSplitter mapSplitter =
        Splitter.on(lineSep).trimResults().withKeyValueSeparator("=");

    String mapString = "key1=value1" + lineSep + "key2=value2" + lineSep + "key3=value3";

    Map<String, String> result = mapSplitter.split(mapString);
    System.out.println(result);
  }

}
