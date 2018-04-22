package com.spike.giantdataanalysis.model.algorithms.search;

/**
 * 按键值有序的符号表.
 * @author zhoujiagen
 */
public interface IOrderedST<Key extends Comparable<Key>, Value> extends IST<Key, Value> {

  /** 最小的键. */
  Key min();

  /** 最大的键. */
  Key max();

  /** 小于等于key的最大键. */
  Key floor(Key key);

  /** 大于等于key的最小键. */
  Key ceiling(Key key);

  /** 排名: 小于key的键的数量. 从0开始. */
  int rank(Key key);

  /** 选择: 排名为k的键. 从0开始. */
  Key select(int k);

  /** 删除最小的键. */
  void deleteMin();

  /** 删除最大的键. */
  void deleteMax();

  /** [low, high]之间键的数量. */
  int size(Key low, Key high);

  /** [low, high]之间的所有键, 已排序. */
  Iterable<Key> keys(Key low, Key high);

  /** 表中所有键的集合, 已排序. */
  @Override
  Iterable<Key> keys();
}
