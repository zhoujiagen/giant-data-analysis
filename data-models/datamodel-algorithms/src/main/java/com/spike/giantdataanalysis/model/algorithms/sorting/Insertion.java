package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.Op;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 插入排序.
 * 
 * <pre>
 * 桥牌的一次一张排序.
 * 
 * 从最左边到当前索引的部分已排序, 但这些元素不一定是最终的位置;
 * 后续遇到更小的元素时, 需要后移以留出位置供该小元素插入.
 * </pre>
 * @author zhoujiagen
 */
public class Insertion<T extends Comparable<T>> extends Op<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    int N = a.length;
    for (int i = 1; i < N; i++) {
      // 将第i个元素放到左边部分已部分排序中的恰当位置: 通过依次左移一个元素
      for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
        exch(a, j, j - 1);
      }

      super.show(a);// DEBUG
    }
  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    Insertion<Character> sorting = new Insertion<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
