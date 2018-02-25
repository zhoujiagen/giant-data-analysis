package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.Op;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;
import com.spike.giantdataanalysis.model.algorithms.support.Statistics;

/**
 * 快速排序.
 * 
 * <pre>
 * 将数组划分为两个子数组, 将两部分独立的排序;
 * 与归并排序不同的是, 当两个子数组都有序时, 整个数组有序了.
 * 
 * 划分操作:a[lo..hi], 返回j
 * a[j]已排定;
 * a[lo..j-1]均不大于a[j];
 * a[j+1..hi]均不小于a[j].
 * </pre>
 * @author zhoujiagen
 */
public class Quick<T extends Comparable<T>> extends Op<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    Statistics.shuffle(a); // 消除输入的依赖

    this.sort(a, 0, a.length - 1);
  }

  private void sort(T[] a, int lo, int hi) {
    if (hi <= lo) {
      return;
    }

    int j = this.partition(a, lo, hi); // 划分
    System.out.println("j=" + j); // DEBUG
    this.sort(a, lo, j - 1);
    this.sort(a, j + 1, hi);

    super.show(a); // DEBUG
  }

  /**
   * 划分为a[lo..j-1], a[j], a[j+1..hi], 使得a[lo..j-1] <= a[j] <= a[j+1..hi].
   * @param a
   * @param lo
   * @param hi
   * @return
   */
  private int partition(T[] a, int lo, int hi) {
    // 分别从左向右/从右向左遍历的索引
    int i = lo;
    int j = hi + 1;

    T v = a[lo]; // 选择第一个元素作为划分基元素v

    while (true) {
      // 从左向右, 找到比v小的元素
      while (super.less(a[++i], v)) {
        if (i == hi) {
          break;
        }
      }
      // 从右向左, 找到比v大的元素
      while (super.less(v, a[--j])) {
        if (j == lo) {
          break;
        }
      }
      if (i >= j) {
        // 两个索引已碰撞, 退出
        break;
      }
      // 交换两个索引上的元素, 以满足基元素两侧左小右大的约束
      super.exch(a, i, j);
      System.out.println("i=" + i + ", j=" + j); // DEBUG
    }

    // 交换第一个元素与从右向左遍历索引上的元素
    super.exch(a, lo, j);
    return j;
  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    Quick<Character> sorting = new Quick<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
