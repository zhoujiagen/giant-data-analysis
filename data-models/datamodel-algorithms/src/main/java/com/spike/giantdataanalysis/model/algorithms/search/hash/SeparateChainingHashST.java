package com.spike.giantdataanalysis.model.algorithms.search.hash;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.search.IST;
import com.spike.giantdataanalysis.model.algorithms.search.SequentialSearchST;

/**
 * 基于拉链法的散列表.
 * 
 * <pre>
 * 将大小为M的数组中每个元素指向一条链表, 其中每个节点为散列值为该元素索引的键值对.
 * 
 * 选择足够大的M, 使得链表尽可能的短, 以保证高效的查找.
 * </pre>
 * @author zhoujiagen
 */
public class SeparateChainingHashST<Key, Value> implements IST<Key, Value> {

  private static int MIN_M = 4; // 最小的散列表大小

  private int N; // 键值对总数
  private int M; // 散列表的大小

  private SequentialSearchST<Key, Value>[] st; // 链表对象的数组

  @SuppressWarnings("unchecked")
  public SeparateChainingHashST(int M) {
    this.M = M;
    st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
    for (int i = 0; i < M; i++) {
      st[i] = new SequentialSearchST<Key, Value>();
    }
  }

  // 计算键的哈希值
  private int hash(Key key) {
    // 对散列表大小M取模
    return (key.hashCode() & 0x7fffffff) % M;
  }

  // 调整散列表的大小
  private void resize(int chains) {
    SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(chains);

    for (int i = 0; i < M; i++) {
      for (Key key : st[i].keys()) {
        temp.put(key, st[i].get(key));
      }
    }

    this.M = temp.M;
    this.N = temp.N;
    this.st = temp.st;
  }

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null key!");
    if (val == null) {
      this.delete(key);
      return;
    }

    if (N >= 10 * M) {
      this.resize(2 * M); // 调整散列表大小
    }

    if (!this.contains(key)) {
      N++;
    }
    st[this.hash(key)].put(key, val);
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    return st[this.hash(key)].get(key);
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    if (this.contains(key)) {
      N--;
    }
    st[this.hash(key)].delete(key);

    if (M > MIN_M && N <= 2 * M) {
      this.resize(M / 2); // 调整散列表的大小
    }
  }

  @Override
  public boolean contains(Key key) {
    Preconditions.checkArgument(key != null, "null key!");

    return st[this.hash(key)].contains(key);
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
      for (Key key : st[i].keys()) {
        queue.enqueue(key);
      }
    }
    return queue;
  }

}
