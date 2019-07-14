package com.spike.giantdataanalysis.commons.guava.collections;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * <pre>
 * {@link BiMap}的单元测试: 双向的Map、保持键和值的唯一性
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestBiMap {

  @Test(expected = IllegalArgumentException.class)
  public void _create() {
    BiMap<String, String> biMap = HashBiMap.create();

    biMap.put("1", "foo");
    // 不允许有重复的值
    biMap.put("2", "foo");
  }

  @Test
  public void _forcePut() {
    BiMap<String, String> biMap = HashBiMap.create();
    biMap.put("1", "foo");
    biMap.forcePut("2", "foo");

    Assert.assertTrue(biMap.containsValue("foo"));
    Assert.assertNull(biMap.get("1"));
    Assert.assertNotNull(biMap.get("2"));

    System.out.println(biMap);
  }

  @Test
  public void _inverse() {
    BiMap<String, String> biMap = HashBiMap.create();

    biMap.put("1", "foo");
    biMap.put("2", "bar");

    BiMap<String, String> inversedBiMap = biMap.inverse();

    Assert.assertTrue(inversedBiMap.containsKey("foo"));
    Assert.assertTrue(inversedBiMap.containsKey("bar"));

    Assert.assertTrue(inversedBiMap.containsValue("1"));
    Assert.assertTrue(inversedBiMap.containsValue("2"));

    System.out.println(inversedBiMap);
  }

}
