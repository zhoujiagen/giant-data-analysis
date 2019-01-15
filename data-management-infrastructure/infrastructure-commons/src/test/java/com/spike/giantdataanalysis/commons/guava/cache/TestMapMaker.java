package com.spike.giantdataanalysis.commons.guava.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.MapMaker;

/**
 * <pre>
 * {@link MapMaker}的单元测试
 * 
 * 用于创建{@link ConcurrentHashMap}
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestMapMaker {

  @Test
  public void create() {
    ConcurrentMap<String, String> result = new MapMaker()//
        .concurrencyLevel(2)// 更新操作的并行度
        .initialCapacity(10)// 初始容量
        .weakValues()// 值为WeakRefernce
        .<String, String> makeMap()//
    ;

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

}
