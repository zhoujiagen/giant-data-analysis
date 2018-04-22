package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.MergingOp;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 归并排序: 自底向上.
 * 
 * <pre>
 * 将数组拆分为sz大小的多个部分, 将各部分分别通过归并排序;
 * 随着sz的增大, 数组由部分有序变为整体有序.
 * </pre>
 * @author zhoujiagen
 */
public class MergeBU<T extends Comparable<T>> extends MergingOp<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    int N = a.length;

    // sz为子数组的大小: 1,2,4,8,...
    for (int sz = 1; sz < N; sz = sz + sz) {
      for (int lo = 0; lo < N - sz; lo += sz + sz) { // lo: 子数组的索引
        super.merge(a, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
      }
      System.out.print("sz=" + sz + " ");
      super.show(a); // DEBUG
    }
  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    MergeBU<Character> sorting = new MergeBU<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
