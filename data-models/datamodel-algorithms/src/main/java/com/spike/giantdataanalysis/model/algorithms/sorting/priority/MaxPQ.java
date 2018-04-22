package com.spike.giantdataanalysis.model.algorithms.sorting.priority;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 基于堆的优先队列.
 * @author zhoujiagen
 */
public class MaxPQ<Key extends Comparable<Key>> extends ReheapifyOp<Key> {

  private Key[] pq; // 堆排序的完全二叉树, pq[0]未使用, 使用pq[1..N-1]
  private int N = 0;

  public MaxPQ() {
  }

  @SuppressWarnings("unchecked")
  public MaxPQ(int maxN) {
    pq = (Key[]) new Comparable[maxN + 1];
  }

  @SuppressWarnings("unchecked")
  public MaxPQ(Key[] a) {
    N = a.length;
    pq = (Key[]) new Comparable[a.length + 1];
    for (int i = 0; i < N; i++) {
      pq[i + 1] = a[i];
    }
    super.show(pq); // DEBUG
    // 堆的构造
    for (int k = N / 2; k >= 1; k--) {
      super.sink(pq, k, N);
    }
    super.show(pq); // DEBUG

    Preconditions.checkState(this.isMaxHeap());
  }

  public void insert(Key v) {

    if (N == pq.length - 1) {
      this.resize(2 * pq.length); // 扩大
    }

    pq[++N] = v; // 加在最后一个位置

    super.swim(pq, N); // 上浮调整堆

    Preconditions.checkState(this.isMaxHeap());
  }

  public Key max() {
    Preconditions.checkArgument(!this.isEmpty(), "empty!");

    return pq[1];
  }

  public Key delMax() {
    Key max = pq[1]; // 顶部元素为最大键

    // 交换第一个和最后一个位置上元素
    super.exch(pq, 1, N--); // 堆减小
    pq[N + 1] = null;

    // 下沉调整堆
    super.sink(pq, 1, N);

    if ((N > 0) && (N == (pq.length - 1) / 4)) {
      this.resize(pq.length / 2); // 缩小
    }
    Preconditions.checkState(this.isMaxHeap());

    return max;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  private void resize(int capacity) {
    Preconditions.checkArgument(capacity > N, "Too small capacity!");

    @SuppressWarnings("unchecked")
    Key[] temp = (Key[]) new Comparable[capacity];
    for (int i = 1; i <= N; i++) {
      temp[i] = pq[i];
    }
    pq = temp;
  }

  /**
   * pq[1..N]是否是最大堆.
   * @return
   */
  private boolean isMaxHeap() {
    return this.isMaxHeap(1);
  }

  // pq[1..N]中以位置k为根的子树是否是最大堆
  private boolean isMaxHeap(int k) {
    if (k > N) return true;

    int left = 2 * k;
    int right = 2 * k + 1;
    if (left <= N && super.less(pq[k], pq[left])) {
      return false;
    }
    if (right <= N && super.less(pq[k], pq[right])) {
      return false;
    }
    return this.isMaxHeap(left) && this.isMaxHeap(right);
  }

  public static void main(String[] args) {
    MaxPQ<Character> pq = new MaxPQ<>(SortingData.data());
    Character c = null;
    while ((c = pq.delMax()) != null) {
      System.out.print(c + " ");
    }
    System.out.println();
  }
}
