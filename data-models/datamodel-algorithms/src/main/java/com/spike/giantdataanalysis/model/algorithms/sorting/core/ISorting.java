package com.spike.giantdataanalysis.model.algorithms.sorting.core;

/**
 * 排序接口.
 * @author zhoujiagen
 */
public interface ISorting<T extends Comparable<T>> {
  /** 执行数组排序操作. */
  void sort(T[] a);
}
