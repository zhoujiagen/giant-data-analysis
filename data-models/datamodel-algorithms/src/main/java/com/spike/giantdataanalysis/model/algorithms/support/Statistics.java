package com.spike.giantdataanalysis.model.algorithms.support;

import java.util.Random;

import com.google.common.base.Preconditions;

/**
 * 概率统计工具.
 * @author zhoujiagen
 */
public class Statistics {

  private static Random random = new Random(System.currentTimeMillis());

  private Statistics() {
  }

  /**
   * 打散数组.
   * @param a
   */
  public static <T extends Comparable<T>> void shuffle(T[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++) {
      int r = i + uniform(N - i); // [i, n-1]
      T temp = a[i];
      a[i] = a[r];
      a[r] = temp;
    }
  }

  /** 随机位. */
  public static boolean bit() {
    return random.nextBoolean();
  }

  /** 均匀分布: [0.0, 1.0) */
  public static double uniform() {
    return random.nextDouble();
  }

  /** 均匀分布: [0.0, n) */
  public static int uniform(int n) {
    return random.nextInt(n);
  }

  /** 均匀分布: [a, b) */
  public static int uniform(int a, int b) {
    Preconditions.checkArgument(a <= b && (b - a) < Integer.MAX_VALUE, //
      "Invalid range: [" + a + "," + b + ")");

    return a + uniform(b - a);
  }

}
