package com.spike.giantdataanalysis.model.algorithms.search;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.commons.annotation.constraint.InvariantConstraint;
import com.spike.giantdataanalysis.model.algorithms.adt.Queue;

/**
 * 有序符号表实现: 基于有序数组的二分查找.
 * @author zhoujiagen
 */
@InvariantConstraint(description = "rank(select(i)) == i")
public class BinarySearchST<Key extends Comparable<Key>, Value> implements IOrderedST<Key, Value> {

  // 平行数组: 键数组, 值数组
  private Key[] keys;
  private Value[] vals;
  private int N = 0; // 键值对的数量

  /** 扩容因子: 扩容后数组大小变为原始数组大小的倍数. */
  public static int GrowFactor = 2;
  /** 收缩检查因子: 数组总大小与栈中元素数量的比例等于该值时, 执行收缩操作. */
  public static int ShrinkCheckFactor = 4;
  /** 收缩因子: 原始数组大小变为收缩后数组大小的倍数. */
  public static int ShrinkFactor = 2;

  @SuppressWarnings("unchecked")
  public BinarySearchST(int capacity) {
    Preconditions.checkArgument(capacity > 0, "invalid capacity, must greater than 0!");

    keys = (Key[]) new Comparable[capacity];
    vals = (Value[]) new Object[capacity];
  }

  // 底层平行数组扩容
  private void resize(int capacity) {
    Preconditions.checkArgument(N <= capacity, "argument capacity is less than current size!");

    @SuppressWarnings("unchecked")
    Key[] newKeys = (Key[]) new Comparable[capacity];
    @SuppressWarnings("unchecked")
    Value[] newVals = (Value[]) new Object[capacity];

    for (int i = 0; i < N; i++) {
      newKeys[i] = keys[i];
      newVals[i] = vals[i];
    }

    keys = newKeys;
    vals = newVals;
  }

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null arugment key!");

    if (val == null) {
      this.delete(key);
      return;
    }

    int i = this.rank(key);

    if (i < N && keys[i].compareTo(key) == 0) {
      // 命中
      vals[i] = val;
    } else {
      // 未命中
      if (N == keys.length) {
        this.resize(keys.length * GrowFactor);// 扩容
      }
      for (int j = N; j > i; j--) {// 后移一位, 保持键序
        keys[j] = keys[j - 1];
        vals[j] = vals[j - 1];
      }
      keys[i] = key;
      vals[i] = val;
      N++;
    }
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    if (this.isEmpty()) {
      return null;
    }

    int i = this.rank(key);
    if (i < N && keys[i].compareTo(key) == 0) {
      // 命中
      return vals[i];
    } else {
      // 未命中
      return null;
    }
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    if (this.isEmpty()) {
      return;
    }

    int i = this.rank(key);
    if (i < N && keys[i].compareTo(key) == 0) {
      // 命中
      for (int j = i; j < N - 1; j++) { // 左移一位, 保持键序
        keys[j] = keys[j + 1];
        vals[j] = vals[j + 1];
      }
      N--;
      keys[N] = null;
      vals[N] = null;

      // 收缩
      if (N > 0 && N == keys.length / ShrinkCheckFactor) {
        this.resize(keys.length / ShrinkFactor);
      }

    } else {
      // 未命中: do nothing
    }

  }

  @Override
  public boolean contains(Key key) {
    return this.get(key) != null;
  }

  @Override
  public boolean isEmpty() {
    return N == 0;
  }

  @Override
  public int size() {
    return N;
  }

  @Override
  public Key min() {
    return keys[0];
  }

  @Override
  public Key max() {
    return keys[N - 1];
  }

  @Override
  public Key floor(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    int i = this.rank(key);
    if (i < N && keys[i].compareTo(key) == 0) {
      // 命中
      return keys[i];
    } else if (i == 0) {
      return null;
    } else {
      return keys[i - 1];
    }
  }

  @Override
  public Key ceiling(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    int i = this.rank(key);
    if (i == N) {
      return null;
    } else {
      return keys[i];
    }
  }

  /**
   * {@inheritDoc}
   * 
   * <pre>
   * (1) 如果表中存在该键(命中), 返回该键的位置, 也就是表中小于它的键的数量;
   * (2) 如果表中不存在该键(未命中), 返回表中小于它的键的数量.
   * </pre>
   * @param key
   * @return
   */
  @InvariantConstraint(description = "返回值rtn < N && keys[rtn].compareTo(key) == 0时命中, 其他情况未命中.")
  @Override
  public int rank(Key key) {
    Preconditions.checkArgument(key != null, "null arguemnt!");

    // 迭代方式实现
    int low = 0;
    int high = N - 1;
    while (low <= high) {
      int mid = low + (high - low) / 2;
      int cmp = key.compareTo(keys[mid]);
      if (cmp < 0) {
        high = mid - 1;
      } else if (cmp > 0) {
        low = mid + 1;
      } else {
        return mid;
      }
    }
    return low;

    // 使用递归方式实现
    // return this.rank(key, 0, N - 1);
  }

  // 递归方式实现
  // 子问题参数: low, high
  int rank(Key key, int low, int high) {
    if (high < low) return low;

    int mid = low + (high - low) / 2;
    int cmp = key.compareTo(keys[mid]);
    if (cmp < 0) {
      return rank(key, low, mid - 1);
    } else if (cmp > 0) {
      return rank(key, mid + 1, high);
    } else {
      return mid;
    }
  }

  @Override
  public Key select(int k) {
    return keys[k];
  }

  @Override
  public void deleteMin() {
    this.delete(this.min());
  }

  @Override
  public void deleteMax() {
    this.delete(this.max());
  }

  @Override
  public int size(Key low, Key high) {
    Preconditions.checkArgument(low != null, "null argument low!");
    Preconditions.checkArgument(high != null, "null argument high!");

    if (low.compareTo(high) > 0) {
      return 0;
    }

    if (this.contains(high)) {
      return this.rank(high) - this.rank(low) + 1;
    } else {
      return this.rank(high) - this.rank(low);
    }
  }

  @Override
  public Iterable<Key> keys(Key low, Key high) {
    Preconditions.checkArgument(low != null, "null argument low!");
    Preconditions.checkArgument(high != null, "null argument high!");

    Queue<Key> q = new Queue<>();
    if (low.compareTo(high) > 0) {
      return q;
    }
    int lowRank = this.rank(low);
    int highRank = this.rank(high);
    for (int i = lowRank; i < highRank; i++) {
      q.enqueue(keys[i]);
    }
    if (this.contains(high)) {
      q.enqueue(keys[highRank]);
    }
    return q;
  }

  @Override
  public Iterable<Key> keys() {
    return this.keys(this.min(), this.max());
  }

  // ======================================== 辅助方法
  /** 检查键数组是否排序. */
  public boolean isSorted() {
    for (int i = 0; i < N - 1; i++) {
      if (keys[i].compareTo(keys[i + 1]) > 0) {
        return false;
      }
    }
    return true;
  }

  /** 检查rank(select(i)) == i. */
  public boolean rankInvariant() {
    for (int i = 0; i < N; i++) {
      if (this.rank(this.select(i)) != i) {
        return false;
      }
      if (keys[i].compareTo(this.select(this.rank(keys[i]))) != 0) {
        return false;
      }
    }
    return true;
  }
}
