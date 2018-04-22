package com.spike.giantdataanalysis.model.algorithms.sorting.priority;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.sorting.core.SortingData;

/**
 * 基于堆的优先队列.
 * @author zhoujiagen
 */
public class MinPQ<Key extends Comparable<Key>> extends ReheapifyOp<Key> {

  private Key[] pq; // 堆排序的完全二叉树, pq[0]未使用, 使用pq[1..N-1]
  private int N = 0;

  public MinPQ() {
  }

  @SuppressWarnings("unchecked")
  public MinPQ(int maxN) {
    pq = (Key[]) new Comparable[maxN + 1];
  }

  @SuppressWarnings("unchecked")
  public MinPQ(Key[] a) {
    N = a.length;
    pq = (Key[]) new Comparable[a.length + 1];
    for (int i = 0; i < N; i++) {
      pq[i + 1] = a[i];
    }
    super.show(pq); // DEBUG
    // 堆的构造
    for (int k = N / 2; k >= 1; k--) {
      this.sink(pq, k, N);
    }
    super.show(pq); // DEBUG

    Preconditions.checkState(this.isMinHeap());
  }

  /**
   * 上浮: 自底向上的堆有序化.
   * @param a
   * @param k
   */
  @Override
  public void swim(Key[] a, int k) {
    // 位置k的父节点在位置k/2
    while (k > 1 && super.greater(a, k / 2, k)) {
      this.exch(a, k / 2, k);
      k = k / 2;
    }
  }

  /**
   * 下沉: 自顶向下的堆有序化.
   * <p>
   * 最大元素位于数组末端后, 待排序数组大小会减少.
   * @param a
   * @param k
   * @param n 堆在数组a中实际使用的大小
   */
  @Override
  public void sink(Key[] a, int k, int n) {
    int N = n;

    while (2 * k <= N) {
      // 位置k的子节点在位置2k和2k+1
      int j = 2 * k;
      if (j < N && super.greater(a, j, j + 1)) {
        j++;
      }
      if (!super.greater(a, k, j)) {
        break;
      }
      this.exch(a, k, j);
      k = j;
    }
  }

  public void insert(Key v) {

    if (N == pq.length - 1) {
      this.resize(2 * pq.length); // 扩大
    }

    pq[++N] = v; // 加在最后一个位置

    this.swim(pq, N); // 上浮调整堆

    Preconditions.checkState(this.isMinHeap());
  }

  public Key min() {
    Preconditions.checkArgument(!this.isEmpty(), "empty!");

    return pq[1];
  }

  public Key delMin() {
    Key min = pq[1]; // 顶部元素为最小键

    // 交换第一个和最后一个位置上元素
    super.exch(pq, 1, N--); // 堆减小
    pq[N + 1] = null;

    // 下沉调整堆
    this.sink(pq, 1, N);

    if ((N > 0) && (N == (pq.length - 1) / 4)) {
      this.resize(pq.length / 2); // 缩小
    }
    Preconditions.checkState(this.isMinHeap());

    return min;
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
   * pq[1..N]是否是最小堆.
   * @return
   */
  private boolean isMinHeap() {
    return this.isMinHeap(1);
  }

  // pq[1..N]中以位置k为根的子树是否是最小堆
  private boolean isMinHeap(int k) {
    if (k > N) return true;

    int left = 2 * k;
    int right = 2 * k + 1;
    if (left <= N && super.greater(pq[k], pq[left])) {
      return false;
    }
    if (right <= N && super.greater(pq[k], pq[right])) {
      return false;
    }
    return this.isMinHeap(left) && this.isMinHeap(right);
  }

  public static void main(String[] args) {
    MinPQ<Character> pq = new MinPQ<>(SortingData.data());
    Character c = null;
    while ((c = pq.delMin()) != null) {
      System.out.print(c + " ");
    }
    System.out.println();
  }
}
