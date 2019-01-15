package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

/**
 * <pre>
 * {@link Multimaps}的单元测试
 * 
 * 单个键可以关联多个值、甚至相同的值
 * </pre>
 *
 * @author zhoujiagen
 * @see ArrayListMultimap
 * @see HashMultimap
 */
public class TestMultimaps {

  private ArrayListMultimap<String, String> arrayListMultimap;

  private HashMultimap<String, String> hashMultimap;

  @Before
  public void setUp() {
    arrayListMultimap = ArrayListMultimap.create();
    arrayListMultimap.put("foo", "1");
    arrayListMultimap.put("foo", "1");
    arrayListMultimap.put("foo", "2");
    arrayListMultimap.put("foo", "3");

    hashMultimap = HashMultimap.create();
    hashMultimap.put("foo", "1");
    hashMultimap.put("foo", "1");
    hashMultimap.put("foo", "2");
    hashMultimap.put("foo", "3");
  }

  @Test
  public void _createArrayListMultimap() {
    List<String> values = Lists.newArrayList("1", "1", "2", "3");

    Assert.assertEquals(values, arrayListMultimap.get("foo"));
  }

  @Test
  public void _createHashMultimap() {
    Set<String> values = hashMultimap.get("foo");

    // 仅保留不同的值
    Assert.assertEquals(3, values.size());
    System.out.println(values);
  }

  @Test
  public void _asMap() {
    Map<String, Collection<String>> map = arrayListMultimap.asMap();
    // {foo=[1, 1, 2, 3]}
    System.out.println(map);

    Map<String, Collection<String>> map2 = hashMultimap.asMap();
    // {foo=[3, 2, 1]}
    System.out.println(map2);

  }

}
