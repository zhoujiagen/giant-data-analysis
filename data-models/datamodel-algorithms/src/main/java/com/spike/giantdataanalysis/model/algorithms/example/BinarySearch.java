package com.spike.giantdataanalysis.model.algorithms.example;

import java.util.Arrays;

/**
 * 问题描述: 二分查找.
 * @author zhoujiagen
 */
public class BinarySearch {

  /**
   * 迭代方式实现: 在数组a中查找key, 返回其所在位置; 未找到返回-1.
   * @param key
   * @param a
   * @return
   */
  public static int rank(int key, int[] a) {
    int low = 0;
    int high = a.length - 1;

    while (low <= high) {
      int mid = low + (high - low) / 2; // 中值

      if (key < a[mid]) {
        // 左半边
        high = mid - 1;
      } else if (key > a[mid]) {
        // 右半边
        low = mid + 1;
      } else {
        // 精确匹配
        return mid;
      }
    }

    // 未找到
    return -1;
  }

  /**
   * 递归方式实现: 在数组a中查找key, 返回其所在位置; 未找到返回-1.
   * @param key
   * @param a
   * @return
   * @see #rank(int, int[])
   */
  public static int rankRecursive(int key, int[] a) {
    return rankRecursive(key, a, 0, a.length - 1);
  }

  /**
   * 递归子问题参数: low, high.
   */
  private static int rankRecursive(int key, int[] a, int low, int high) {
    if (low > high) {
      // 未找到
      return -1;
    }

    int mid = low + (high - low) / 2; // 中值
    if (key < a[mid]) {
      // 左半边
      return rankRecursive(key, a, low, mid - 1);
    } else if (key > a[mid]) {
      // 右半边
      return rankRecursive(key, a, mid + 1, high);
    } else {
      // 精确匹配
      return mid;
    }
  }

  public static void main(String[] args) {
    // List<Integer> l = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    // Collections.shuffle(l);
    // System.out.println(l);
    int[] a = new int[] { 1, 9, 2, 5, 6, 7, 4, 8, 3 };
    Arrays.sort(a); // 排序

    int key = 5;
    System.out.println(rank(key, a));
    System.out.println(rankRecursive(key, a));

    System.out.println();

    key = -5;
    System.out.println(rank(key, a));
    System.out.println(rankRecursive(key, a));
  }

}
