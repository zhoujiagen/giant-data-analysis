package com.spike.giantdataanalysis.text.elasticsearch.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.carrotsearch.randomizedtesting.RandomizedTest;

/**
 * <pre>
 * 随机化测试
 * 
 * Core Concepts: https://github.com/randomizedtesting/randomizedtesting/wiki/Core-Concepts
 * More examples: https://github.com/randomizedtesting/randomizedtesting/tree/master/examples/maven/src/main/java/com/carrotsearch/examples/randomizedrunner
 * </pre>
 * @author zhoujiagen
 */
// 将失败堆栈中的seed值添加到此处
// @Seed("E2304CC04E5FC531:BA62B07497C22405")
@RunWith(RandomizedRunner.class)
public class SampleRandomizedtestingTest extends RandomizedTest {

  @Test
  public void dummy() {
    int a = 2;
    Assert.assertEquals(2, a);
  }

  @Test
  public void testAdd() {
    int a = randomIntBetween(0, Integer.MAX_VALUE);
    int b = randomIntBetween(0, Integer.MAX_VALUE);
    int result = add(a, b);
    Assert.assertTrue(result >= a && result >= b);
  }

  private static int add(int a, int b) {
    return a + b;
  }
}
