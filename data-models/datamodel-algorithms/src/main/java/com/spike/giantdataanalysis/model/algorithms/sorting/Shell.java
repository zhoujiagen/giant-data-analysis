package com.spike.giantdataanalysis.model.algorithms.sorting;

import com.spike.giantdataanalysis.model.algorithms.sorting.core.ISorting;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.Op;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 希尔排序.
 * 
 * <pre>
 * 插入排序的扩展: 一次比较移动相邻的元素 => 先将多个相距较远的h序列分别排序, 使得部分有序, 最终使用插入排序完成.
 * 
 * </pre>
 * @author zhoujiagen
 */
public class Shell<T extends Comparable<T>> extends Op<T> implements ISorting<T> {

  @Override
  public void sort(T[] a) {
    int N = a.length;
    int h = 1; // h序列(h为序列中元素数量)

    while (h < N / 3) {
      h = h * 3 + 1;// 1,4,13,40,121,364,...
    }

    super.show(a); // DEBUG
    while (h >= 1) {
      System.out.println("h=" + h); // DEBUG

      // h序列排序
      for (int i = h; i < N; i++) {
        // 将a[i]插入a[i-h], a[i-2h], a[i-3h]...
        for (int j = i; j >= h && super.less(a[j], a[j - h]); j -= h) {
          super.exch(a, j, j - h);
        }
      }
      super.show(a); // DEBUG

      h = h / 3;
    }

  }

  public static void main(String[] args) {
    Character[] a = SortingData.data();
    Shell<Character> sorting = new Shell<>();
    sorting.sort(a);
    System.out.println(sorting.isSorted(a));
    sorting.show(a);
  }
}
