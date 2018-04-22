package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;
import com.spike.giantdataanalysis.model.algorithms.sorting.priority.ReheapifyOp;

/**
 * 堆排序.
 * 
 * <pre>
 * 两个阶段:
 * (1) 堆的构造
 * 如果一个节点的两个子节点已经是堆了, 那么在该节点上调用sink()可以将它变为一个堆.
 * 从右向左, 在数组的前半部分调用sink()构造堆.
 * 
 * (2) 下沉排序
 * 将堆中最大元素与数组最后一个位置元素交换, 缩小堆, 通过下沉操作使得堆有序化.
 * </pre>
 * @author zhoujiagen
 */
public class Heap<T extends Comparable<T>> extends ReheapifyOp<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    int N = a.length;

    // 堆的构造
    for (int k = N / 2; k >= 1; k--) {
      this.sink(a, k, N);
    }

    // 下沉排序: 将a[1..N]排序
    // 注意: 这里将exch()和less()中索引减一以实现a[0..N-1]的排序
    while (N > 1) {
      this.exch(a, 1, N--);
      this.sink(a, 1, N);
    }
  }

  @Override
  public boolean less(T[] a, int i, int j) {
    return a[i - 1].compareTo(a[j - 1]) < 0;
  }

  @Override
  public void exch(T[] a, int i, int j) {
    T t = a[i - 1];
    a[i - 1] = a[j - 1];
    a[j - 1] = t;
  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    Heap<Character> sorting = new Heap<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
