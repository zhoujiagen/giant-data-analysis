package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * <pre>
 * {@link Lists}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestLists {

  /**
   * <pre>
   * 创建
   * </pre>
   */
  @Test
  public void _new() {
    List<String> list = Lists.newArrayList();

    Assert.assertNotNull(list);
  }

  /**
   * <pre>
   * 分区
   * </pre>
   */
  @Test
  public void _partition() {
    List<String> list = Lists.newArrayList("1", "2", "3", "4", "5");

    List<List<String>> listOfList = Lists.partition(list, 3);

    Assert.assertThat(listOfList.size(), Is.is(2));
    // [[1, 2, 3], [4, 5]]
    System.out.println(listOfList);
  }

}
