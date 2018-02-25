package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.Op;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 选择排序.
 * 
 * <pre>
 * 总是选择剩余的未排序的元素中最小的元素.
 * 
 * 首先, 找到数组中最小的元素, 将其与第一个元素交换;
 * 然后, 找到第二小的元素, 将其与第二个元素交换;
 * 直到整个数组已排序.
 * </pre>
 * @author zhoujiagen
 */
public class Selection<T extends Comparable<T>> extends Op<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    int N = a.length;
    for (int i = 0; i < N; i++) {
      int min = i;
      // 定位[i, N-1]中最小元素的位置
      for (int j = i + 1; j < N; j++) {
        if (super.less(a[j], a[min])) {
          min = j;
        }
      }
      // 将第i小的元素放在a[i]中
      super.exch(a, i, min);

      super.show(a);// DEBUG
    }
  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    Selection<Character> sorting = new Selection<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
