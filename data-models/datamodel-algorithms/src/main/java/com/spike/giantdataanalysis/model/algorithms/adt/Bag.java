package com.spike.giantdataanalysis.model.algorithms.adt;

import java.util.Iterator;

/**
 * 袋: 元素无序可重复.
 * @author zhoujiagen
 */
public class Bag<Item> implements Iterable<Item> {

  /** 节点: 基于链表的实现. */
  private class Node {
    Item item;
    Node next;
  }

  private int N = 0; // 元素数量

  private class BagIterator implements Iterator<Item> {
    private Node current;

    BagIterator(Node start) {
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

  private Node first;// 链表头部节点

  @Override
  public Iterator<Item> iterator() {
    return new BagIterator(first);
  }

  /** 构造空袋. */
  public Bag() {
  }

  /** 添加一个元素. */
  public void add(Item item) {
    // 从链表头部添加元素
    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.next = oldFirst;

    N++;
  }

  /** 是否为空袋. */
  public boolean isEmpty() {
    return N == 0;
  }

  /** 袋中元素数量. */
  public int size() {
    return N;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Bag[ ");
    for (Node x = first; x != null; x = x.next) {
      sb.append(x.item + " ");
    }
    sb.append("]");
    return sb.toString();
  }
}
