package com.spike.giantdataanalysis.model.algorithms.adt;

import java.util.Iterator;

import com.google.common.base.Joiner;

/**
 * 先进先出(FIFO)队列.
 * @author zhoujiagen
 */
public class Queue<Item> implements Iterable<Item> {

  /** 节点: 基于链表的实现. */
  private class Node {
    Item item;
    Node next;

    @Override
    public String toString() {
      return item.toString();
    }
  }

  private class QueueIterator implements Iterator<Item> {
    private Node current;

    QueueIterator(Node start) {
      current = start;
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  private Node first;// 最早添加的节点
  private Node last;// 最近添加的节点
  private int N = 0; // 元素数量

  @Override
  public Iterator<Item> iterator() {
    return new QueueIterator(first);
  }

  /** 构造空队列. */
  public Queue() {
  }

  /** 添加一个元素. */
  public void enqueue(Item item) {
    // 向链表尾部添加元素
    Node oldLast = last;
    last = new Node();
    last.item = item;
    last.next = null;

    if (this.isEmpty()) {
      first = last;// 头尾指向唯一的一个元素
    } else {
      oldLast.next = last;
    }

    N++;
  }

  /** 删除最早添加的元素. */
  public Item dequeue() {
    // 从链表头部删除元素
    Item item = first.item;
    first = first.next;

    if (this.isEmpty()) {
      last = null;
    }

    N--;
    return item;
  }

  /** 是否为空队列. */
  public boolean isEmpty() {
    return N == 0; // 或者first == null;
  }

  /** 队列中元素数量. */
  public int size() {
    return N;
  }

  @Override
  public String toString() {
    if (this.isEmpty()) {
      return "Queue [empty]";
    } else {
      return "Queue [" + Joiner.on(" ").join(this).toString() + "]";
    }
  }
}
