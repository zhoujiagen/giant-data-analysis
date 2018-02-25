package com.spike.giantdataanalysis.model.algorithms.example;

import java.util.Arrays;

/**
 * 问题描述: 统计整数数组中和为0的三整数元组的数量.
 * @author zhoujiagen
 */
public class ThreeSum {

  public static int basicSolution(int[] a) {
    int N = a.length;
    int cnt = 0;

    // 三层循环
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        for (int k = j + 1; k < N; k++) {
          if (a[i] + a[j] + a[k] == 0) {
            // System.err.println("Found: " + a[i] + ", " + a[j] + ", " + a[k]);
            cnt++;
          }
        }
      }
    }
    return cnt;
  }

  // N^3 => N^2 * logN
  public static int fastSolution(int[] a) {
    Arrays.sort(a); // 排序

    int N = a.length;
    int cnt = 0;
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        // 二分查找: N => logN
        if (BinarySearch.rank(-a[i] - a[j], a) > j) {
          cnt++;
        }
      }
    }
    return cnt;
  }
}
