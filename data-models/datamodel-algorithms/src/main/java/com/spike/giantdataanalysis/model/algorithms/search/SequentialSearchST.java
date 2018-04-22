package com.spike.giantdataanalysis.model.algorithms.search;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.adt.Bag;

/**
 * 符号表实现: 基于无序链表的顺序查找.
 * @author zhoujiagen
 */
public class SequentialSearchST<Key, Value> implements IST<Key, Value> {

  private Node first; // 链表的首节点
  private int N = 0;// 键值对的数量

  private class Node {
    Key key;
    Value val;
    Node next;

    public Node(Key key, Value val, Node next) {
      super();
      this.key = key;
      this.val = val;
      this.next = next;
    }
  }

  public SequentialSearchST() {
  }

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null key!");
    if (val == null) { // 不允许值为null
      this.delete(key);
      return;
    }

    // 从链表头部依次搜索
    for (Node x = first; x != null; x = x.next) {
      if (key.equals(x.key)) {// 命中
        x.val = val;
        return;
      }
    }

    // 未命中: 放在链表头部
    first = new Node(key, val, first);
    N++;
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    for (Node x = first; x != null; x = x.next) {
      if (key.equals(x.key)) {// 命中
        return x.val;
      }
    }

    return null;// 未命中
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");
    first = this.delete(first, key);
  }

  // 从节点node开始删除键key, 返回删除后开始节点
  private Node delete(Node node, Key key) {
    if (node == null) {
      return null;
    }

    if (key.equals(node.key)) {// 命中: 返回下一节点
      N--;
      return node.next;
    } else {// 未命中: 返回当前节点, 从下一节点开始删除
      node.next = this.delete(node.next, key);
      return node;
    }
  }

  @Override
  public boolean contains(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

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
  public Iterable<Key> keys() {
    Bag<Key> keys = new Bag<>();
    for (Node x = first; x != null; x = x.next) {
      keys.add(x.key);
    }
    return keys;
  }

}
