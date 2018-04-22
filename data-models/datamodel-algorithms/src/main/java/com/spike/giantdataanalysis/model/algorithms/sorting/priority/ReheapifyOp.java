package com.spike.giantdataanalysis.model.algorithms.sorting.priority;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.Op;

/**
 * 堆的有序化操作(最大堆).
 * 
 * <pre>
 * 堆有序: 当一颗二叉树的每个节点都大于等于它的两个子节点时, 它被称为堆有序.
 * 二叉堆: 一组能够用堆有序的完全二叉树排序的元素, 在数组中按照层级存储(不使用数组的第一个位置).
 * 
 * 实现:
 * 大小为N的堆, 使用大小为N+1的数组, 数组中第一个元素未使用;
 * 按完全二叉树形式表示, 在位置k的元素节点的两个子节点在位置2k和2k+1, 在位置k的元素节点的父节点在位置k/2.
 * </pre>
 * @author zhoujiagen
 */
public class ReheapifyOp<T extends Comparable<T>> extends Op<T> {

  /**
   * 上浮: 自底向上的堆有序化.
   * @param a
   * @param k
   */
  public void swim(T[] a, int k) {
    // 位置k的父节点在位置k/2
    while (k > 1 && this.less(a, k / 2, k)) {
      this.exch(a, k / 2, k);
      k = k / 2;
    }
  }

  /**
   * 下沉: 自顶向下的堆有序化.
   * <p>
   * 最大元素位于数组末端后, 待排序数组大小会减少.
   * @param a
   * @param k
   * @param n 堆在数组a中实际使用的大小
   */
  public void sink(T[] a, int k, int n) {
    int N = n;

    while (2 * k <= N) {
      // 位置k的子节点在位置2k和2k+1
      int j = 2 * k;
      if (j < N && this.less(a, j, j + 1)) {
        j++;
      }
      if (!this.less(a, k, j)) {
        break;
      }
      this.exch(a, k, j);
      k = j;
    }
  }
}
