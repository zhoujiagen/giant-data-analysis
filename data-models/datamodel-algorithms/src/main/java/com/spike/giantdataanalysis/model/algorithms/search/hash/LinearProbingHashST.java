package com.spike.giantdataanalysis.model.algorithms.search.hash;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.search.IST;

/**
 * 基于线性探测的散列表.
 * 
 * <pre>
 * 开放地址散列表: 使用数组中的空位解决碰撞冲突.
 * 
 * 线性探测法
 * 当碰撞发生时, 直接检查散列表中的下一个位置(索引值加1), 产生结果:
 * (1) 命中, 该位置的键与被查找的键相同;
 * (2) 未命中, 该位置没有键;
 * (3) 继续查找, 该位置的键与被查找的键不同.
 * </pre>
 * @author zhoujiagen
 */
public class LinearProbingHashST<Key, Value> implements IST<Key, Value> {

  private int N; // 键值对的总数
  private int M = 16; // 线性探测表的大小

  // 并行数组
  private Key[] keys; // 键
  private Value[] vals; // 值

  @SuppressWarnings("unchecked")
  public LinearProbingHashST(int capacity) {
    this.M = capacity;
    this.keys = (Key[]) new Object[capacity];
    this.vals = (Value[]) new Object[capacity];
  }

  // 计算键的哈希值
  private int hash(Key key) {
    // 对线性探测表大小M取模
    return (key.hashCode() & 0x7fffffff) % M;
  }

  // 调整线性探测表的大小
  private void resize(int capacity) {
    LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<>(capacity);
    for (int i = 0; i < M; i++) {
      if (keys[i] != null) {
        temp.put(keys[i], vals[i]);
      }
    }
    this.keys = temp.keys;
    this.vals = temp.vals;
    this.M = capacity;
  }

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null key!");
    if (val == null) {
      this.delete(key);
      return;
    }

    if (N >= M / 2) {
      this.resize(M * 2); // 调整线性探测表的大小
    }

    int i;
    // keys[i]==null表示未命中, +1为继续查找
    for (i = this.hash(key); keys[i] != null; i = (i + 1) % M) {
      if (keys[i].equals(key)) {
        // 命中
        vals[i] = val;
        return;
      }
    }

    // 未命中
    keys[i] = key;
    vals[i] = val;
    N++;
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    // keys[i]==null表示未命中, +1为继续查找
    for (int i = this.hash(key); keys[i] != null; i = (i + 1) % M) {
      if (keys[i].equals(key)) {
        // 命中
        return vals[i];
      }
    }

    // 未命中
    return null;
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    if (!this.contains(key)) {
      return;
    }
    // 定位键所在位置, 平行数组置为null
    int i = this.hash(key);
    while (!key.equals(keys[i])) {
      i = (i + 1) % M;
    }
    keys[i] = null;
    vals[i] = null;

    // 后续的重放入
    i = (i + 1) % M;
    while (keys[i] != null) {
      Key redoKey = keys[i];
      Value redoVal = vals[i];
      keys[i] = null;
      vals[i] = null;
      N--;
      this.put(redoKey, redoVal);
      i = (i + 1) % M;
    }

    N--;

    if (N > 0 && N == M / 8) {
      this.resize(M / 2);
    }
  }

  @Override
  public boolean contains(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    return this.get(key) == null;
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
  public Iterable<Key> keys() {
    Queue<Key> queue = new Queue<>();
    for (int i = 0; i < M; i++) {
      if (keys[i] != null) {
        queue.enqueue(keys[i]);
      }
    }
    return queue;
  }

}
