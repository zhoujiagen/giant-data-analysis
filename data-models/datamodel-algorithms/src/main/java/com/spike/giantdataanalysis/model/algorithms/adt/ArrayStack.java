package com.spike.giantdataanalysis.model.algorithms.adt;

import java.util.Iterator;

import com.spike.giantdataanalysis.model.algorithms.support.Out;

/**
 * 基于数组实现的栈.
 * @author zhoujiagen
 */
public class ArrayStack<Item> implements Iterable<Item> {

  /** 栈迭代器. */
  private class ArrayStackIterator implements Iterator<Item> {
    private int n;

    ArrayStackIterator(int size) {
      n = size;
    }

    @Override
    public boolean hasNext() {
      return n > 0;
    }

    @Override
    public Item next() {
      return a[--n]; // 后进先出, 共享内部数组
    }
  }

  @SuppressWarnings("unchecked")
  private Item[] a = (Item[]) new Object[1]; // 内部数组, 使用了模拟泛型的实例化
  private int N = 0;// 元素数量

  /** 扩容因子: 扩容后数组大小变为原始数组大小的倍数. */
  public static int GrowFactor = 2;
  /** 收缩检查因子: 数组总大小与栈中元素数量的比例等于该值时, 执行收缩操作. */
  public static int ShrinkCheckFactor = 4;
  /** 收缩因子: 原始数组大小变为收缩后数组大小的倍数. */
  public static int ShrinkFactor = 2;

  @Override
  public Iterator<Item> iterator() {
    return new ArrayStackIterator(N);
  }

  /** 构造空栈. */
  public ArrayStack() {
  }

  /** 添加一个元素. */
  public void push(Item item) {
    if (N == a.length) {
      // 扩容
      this.resize(a.length * GrowFactor);
    }

    a[N++] = item;
  }

  /** 删除最近添加的元素. */
  public Item pop() {
    Item item = a[--N];
    a[N] = null;// 避免对象游离: 被弹出的元素的引用仍在数组中

    if (N > 0 && N == a.length / ShrinkCheckFactor) {
      this.resize(a.length / ShrinkFactor);
    }
    return item;
  }

  private void resize(int max) {
    // 将原始数组移动到大小为max的新数组中
    // String action = "";
    // if (a.length < max) {
    // action = "grow";
    // } else {
    // action = "shrink";
    // }
    // System.err.print("DEBUG>>> BEFORE: " + this.toString() + //
    // " " + action + " array from " + a.length + " to " + max);

    @SuppressWarnings("unchecked")
    Item[] temp = (Item[]) new Object[max];
    for (int i = 0; i < N; i++) {
      temp[i] = a[i];
    }
    a = temp;// 将原始数组的引用指向新数组的引用

    // System.err.println(", AFTER: " + this.toString());
  }

  /** 是否为空栈. */
  public boolean isEmpty() {
    return N == 0;
  }

  /** 栈中元素数量. */
  public int size() {
    return N;
  }

  @Override
  public String toString() {
    return "ArrayStack(N=" + N + ", a.length=" + a.length + ", a=[" + Out.out(a) + "])";
  }
}
