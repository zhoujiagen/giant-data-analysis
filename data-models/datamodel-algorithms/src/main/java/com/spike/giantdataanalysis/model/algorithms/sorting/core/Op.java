package com.spike.giantdataanalysis.model.algorithms.sorting.core;

/**
 * 排序算法中使用的操作: 比较, 交换元素等.
 * @author zhoujiagen
 */
public class Op<T extends Comparable<T>> {

  /** <strong>比较操作</strong>: 比较两个元素的大小, v是否小于w. */
  protected boolean less(T v, T w) {
    return v.compareTo(w) < 0;
  }

  protected boolean greater(T v, T w) {
    return v.compareTo(w) > 0;
  }

  /** <strong>比较操作</strong>: 比较两个元素的大小, 位置i的元素是否小于位置j的元素. */
  protected boolean less(T[] a, int i, int j) {
    return a[i].compareTo(a[j]) < 0;
  }

  protected boolean greater(T[] a, int i, int j) {
    return a[i].compareTo(a[j]) > 0;
  }

  /** <strong>交换操作</strong>: 交换数组中两个元素的位置. */
  protected void exch(T[] a, int i, int j) {
    T t = a[i];
    a[i] = a[j];
    a[j] = t;
  }

  /** 检测数组是否已排序. */
  public boolean isSorted(T[] a) {
    for (int i = 1, len = a.length; i < len; i++) {
      if (this.less(a[i], a[i - 1])) {
        return false;
      }
    }
    return true;
  }

  /** 数组数组内容. */
  public void show(T[] a) {
    for (T t : a) {
      System.out.print(t + " ");
    }
    System.out.println();
  }
}
