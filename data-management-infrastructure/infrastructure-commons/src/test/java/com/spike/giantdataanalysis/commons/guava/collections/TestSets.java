package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.Set;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * <pre>
 * {@link Sets}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestSets {

  private Set<String> set1 = Sets.newHashSet("1", "2", "3");
  private Set<String> set2 = Sets.newHashSet("2", "3", "4");

  @Test
  public void _difference() {
    SetView<String> resultSet = Sets.difference(set1, set2);

    Assert.assertThat(resultSet.size(), Is.is(1));
    System.out.println(resultSet);// [1]
  }

  /**
   * <pre>
   * 对称差
   * </pre>
   */
  @Test
  public void _symmetricDifference() {
    SetView<String> resultSet = Sets.symmetricDifference(set1, set2);

    Assert.assertThat(resultSet.size(), Is.is(2));
    System.out.println(resultSet);// [1, 4]
  }

  @Test
  public void _intersection() {
    SetView<String> resultSet = Sets.intersection(set1, set2);

    Assert.assertThat(resultSet.size(), Is.is(2));
    System.out.println(resultSet);// [3, 2]
  }

  @Test
  public void _union() {
    SetView<String> resultSet = Sets.union(set1, set2);

    Assert.assertThat(resultSet.size(), Is.is(4));
    System.out.println(resultSet);// [3, 2, 1, 4]
  }

}
