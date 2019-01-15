package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <pre>
 * 不可变集合的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestImmutableCollection {

  @SuppressWarnings("deprecation")
  @Test(expected = UnsupportedOperationException.class)
  public void immutableMap() {
    Map<String, String> map = Maps.newHashMap();
    map.put("1", "foo");
    map.put("2", "bar");
    ImmutableMap<Object, Object> immutableMap = ImmutableMap.builder().putAll(map).build();

    Assert.assertTrue(immutableMap.containsKey("1"));

    immutableMap.put("3", "baz");
  }

  @Test
  public void immutableMultiMap() {
    ImmutableListMultimap<String, String> immutableMultimap = ImmutableListMultimap//
        .<String, String> builder()//
        .putAll("1", "11", "12", "13")//
        .put("2", "22")//
        .build();

    List<String> oneValues = Lists.newArrayList("11", "12", "13");
    // 操作返回的也是不可变集合
    Assert.assertTrue(immutableMultimap.get("1") instanceof ImmutableList);
    Assert.assertEquals(oneValues, immutableMultimap.get("1"));
  }
}
