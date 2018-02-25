package com.spike.giantdataanalysis.model.algorithms.adt;

import java.util.Iterator;

/**
 * 后进先出(LIFO)栈.
 * @author zhoujiagen
 */
public class Stack<Item> implements Iterable<Item> {

  /** 节点: 基于链表的实现. */
  private class Node {
    Item item;
    Node next;
  }

  /** 栈迭代器. */
  private class StackIterator implements Iterator<Item> {
    private Node current;

    StackIterator(Node start) {
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

  private Node first;// 栈顶元素: 最近添加的元素
  private int N = 0; // 元素数量

  @Override
  public Iterator<Item> iterator() {
    return new StackIterator(first);
  }

  /** 构造空栈. */
  public Stack() {
  }

  /** 添加一个元素. */
  public void push(Item item) {
    // 向栈顶添加元素
    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.next = oldFirst;
    N++;
  }

  /** 删除最近添加的元素. */
  public Item pop() {
    // 从栈顶删除元素
    Item item = first.item;
    first = first.next;
    N--;
    return item;
  }

  /** 是否为空栈. */
  public boolean isEmpty() {
    return N == 0; // 或者first == null
  }

  /** 栈中元素数量. */
  public int size() {
    return N;
  }
}
