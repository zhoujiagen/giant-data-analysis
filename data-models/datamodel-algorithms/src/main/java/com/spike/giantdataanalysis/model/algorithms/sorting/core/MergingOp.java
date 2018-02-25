package com.spike.giantdataanalysis.model.algorithms.sorting.core;

/**
 * 归并数组操作.
 * @author zhoujiagen
 */
public class MergingOp<T extends Comparable<T>> extends Op<T> {

  /** 辅助数组. */
  protected T[] aux;

  /**
   * 将a[lo..mid]与a[mid+1..hi]合并.
   * @param a
   * @param lo
   * @param mid
   * @param hi
   */
  @SuppressWarnings("unchecked")
  public void merge(T[] a, int lo, int mid, int hi) {
    // 两个数组索引
    int i = lo; // [lo, mid]
    int j = mid + 1; // [mid+1, hi]

    // 构造辅助数组aux
    if (aux == null) {
      aux = (T[]) new Comparable[a.length];
    }
    for (int k = lo; k <= hi; k++) {
      aux[k] = a[k];
    }

    // 归并到数组a中
    for (int k = lo; k <= hi; k++) {
      if (i > mid) {
        a[k] = aux[j++];
      } else if (j > hi) {
        a[k] = aux[i++];
      } else if (this.less(aux[j], aux[i])) {
        a[k] = aux[j++];
      } else {
        a[k] = aux[i++];
      }
    }
  }
}
