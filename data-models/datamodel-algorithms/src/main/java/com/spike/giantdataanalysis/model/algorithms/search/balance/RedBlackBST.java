package com.spike.giantdataanalysis.model.algorithms.search.balance;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.search.IOrderedST;

/**
 * 红黑树.
 * 
 * <pre>
 * 两种链接: 红链接将两个2-节点连接起来构成一个3-节点, 黑链接是2-3树中普通链接.
 * 
 * 红黑树是含有红黑链接的二叉查找树, 且:
 * (a) 红链接均为左链接;
 * (b) 没有一个节点同时和两条红链接相连;
 * (c) 完美黑色平衡的.
 * 
 * 旋转操作: 操作中可能会出现红色右链接或者两条连续的红链接, 需要旋转修复.
 * (1) 左旋转: 将一条红右链接转化为左链接;
 * (2) 右旋转: 将一条红左链接转化为右链接.
 * 旋转操作需要保持: 有序性和完美平衡性.
 * 
 * TODO(zhoujiagen) need more attention on red black BST, especially on delete operation!
 * </pre>
 * @author zhoujiagen
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> implements IOrderedST<Key, Value> {

  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private class Node {
    private Key key; // 键
    private Value val; // 值
    private Node left, right; // 左右子树
    // 父节点指向该节点的链接的颜色, 即节点的颜色为指向该节点的链接的颜色; 空链接为黑色
    private boolean color;
    private int N; // 以该节点为根的子树中节点总数

    public Node(Key key, Value val, boolean color, int N) {
      this.key = key;
      this.val = val;
      this.color = color;
      this.N = N;
    }
  }

  private Node root; // 根节点

  public RedBlackBST() {
  }

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null key!");
    if (val == null) {
      this.delete(key);
      return;
    }

    root = this.put(root, key, val);
    root.color = BLACK; // 根节点总为黑色

  }

  private Node put(Node h, Key key, Value val) {
    if (h == null) {
      return new Node(key, val, RED, 1); // 和父节点用红链接相连
    }

    int cmp = key.compareTo(h.key);
    if (cmp < 0) {
      h.left = this.put(h.left, key, val);
    } else if (cmp > 0) {
      h.right = this.put(h.right, key, val);
    } else {
      h.val = val;
    }

    // 单个右红链接
    if (this.isRed(h.right) && !this.isRed(h.left)) {
      h = this.rotateLeft(h);
    }
    // 连续两条左红链接
    if (this.isRed(h.left) && this.isRed(h.left.left)) {
      h = this.rotateRight(h);
    }
    // 左右红链接
    if (this.isRed(h.left) && this.isRed(h.right)) {
      this.flipColor(h);
    }

    h.N = this.size(h.left) + this.size(h.right) + 1;
    return h;
  }

  // 子节点由红变黑, 父节点由黑变红
  private void flipColor(Node h) {
    h.color = !h.color;
    h.left.color = !h.left.color;
    h.right.color = !h.right.color;
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    return this.get(root, key);
  }

  private Value get(Node x, Key key) {
    while (x != null) {
      int cmp = key.compareTo(x.key);
      if (cmp < 0) {
        x = x.left;
      } else if (cmp > 0) {
        x = x.right;
      } else {
        return x.val;
      }
    }
    return null;
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkState(!this.isEmpty(), "empty!");
    if (!this.contains(key)) {
      return;
    }

    // 左右子链接均为黑色, 将根节点置为红色
    if (!this.isRed(root.left) && !this.isRed(root.right)) {
      root.color = RED;
    }

    root = this.delete(root, key);
    if (!this.isEmpty()) {
      root.color = BLACK;
    }
  }

  private Node delete(Node h, Key key) {
    if (key.compareTo(h.key) < 0) {

      if (!this.isRed(h.left) && !this.isRed(h.left.left)) {
        h = moveRedLeft(h);
      }
      h.left = this.delete(h.left, key);

    } else {

      if (this.isRed(h.left)) {
        h = this.rotateRight(h);
      }
      if (key.compareTo(h.key) == 0 && (h.right == null)) {
        return null;
      }
      if (!this.isRed(h.right) && !this.isRed(h.right.left)) {
        h = this.moveRedRight(h);
      }
      if (key.compareTo(h.key) == 0) {
        Node x = this.min(h.right);
        h.key = x.key;
        h.val = x.val;
        h.right = this.deleteMin(h.right);
      } else {
        h.right = this.delete(h.right, key);
      }
    }

    return this.balance(h);
  }

  @Override
  public boolean contains(Key key) {
    return this.get(key) != null;
  }

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public int size() {
    return this.size(root);
  }

  @Override
  public Key min() {
    Preconditions.checkState(!this.isEmpty(), "empty!");
    return this.min(root).key;
  }

  // 以x为根的子树中最小键节点
  private Node min(Node x) {
    if (x.left == null) {
      // 直到左链接为空
      return x;
    } else {
      return this.min(x.left);
    }
  }

  @Override
  public Key max() {
    Preconditions.checkState(!this.isEmpty(), "empty!");

    return this.max(root).key;
  }

  private Node max(Node x) {
    if (x.right == null) {
      // 直到右链接为空
      return x;
    } else {
      return this.max(x.right);
    }
  }

  @Override
  public Key floor(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");
    Preconditions.checkState(!this.isEmpty(), "empty!");

    Node x = this.floor(root, key);
    if (x == null) {
      return null;
    } else {
      return x.key;
    }
  }

  private Node floor(Node x, Key key) {
    if (x == null) {
      return null;
    }
    int cmp = key.compareTo(x.key);
    if (cmp == 0) {
      return x;
    } else if (cmp < 0) {
      return this.floor(x.left, key);
    } else {
      Node t = this.floor(x.right, key);
      if (t != null) {
        return t;
      } else {
        return x;
      }
    }
  }

  @Override
  public Key ceiling(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");
    Preconditions.checkState(!this.isEmpty(), "empty!");

    Node x = this.ceiling(root, key);
    if (x == null) {
      return null;
    } else {
      return x.key;
    }
  }

  private Node ceiling(Node x, Key key) {
    if (x == null) {
      return null;
    }
    int cmp = key.compareTo(x.key);
    if (cmp == 0) {
      return x;
    } else if (cmp > 0) {
      return ceiling(x.right, key);
    } else {
      Node t = ceiling(x.left, key);
      if (t != null) {
        return t;
      } else {
        return x;
      }
    }
  }

  @Override
  public int rank(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");
    return this.rank(key, root);
  }

  private int rank(Key key, Node x) {
    if (x == null) {
      return 0;
    }
    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      return this.rank(key, x.left);
    } else if (cmp > 0) {
      return 1 + this.size(x.left) + this.rank(key, x.right);
    } else {
      return this.size(x.left);
    }
  }

  @Override
  public Key select(int k) {
    Preconditions.checkArgument(k > 0 && k < this.size(), "invalid argument!");

    Node x = this.select(root, k);
    return x.key;
  }

  private Node select(Node x, int k) {
    int t = this.size(x.left);
    if (t > k) {
      return this.select(x.left, k);
    } else if (t < k) {
      return this.select(x.right, k - t - 1);
    } else {
      return x;
    }
  }

  @Override
  public void deleteMin() {
    Preconditions.checkState(!this.isEmpty(), "empty!");

    // 左右子链接均为黑色, 将根节点置为红色
    if (!this.isRed(root.left) && !this.isRed(root.right)) {
      root.color = RED;
    }

    root = deleteMin(root);
    if (!this.isEmpty()) {
      root.color = BLACK;
    }
  }

  private Node deleteMin(Node h) {
    if (h.left == null) {
      return null;
    }

    if (!isRed(h.left) && !isRed(h.left.left)) {
      h = moveRedLeft(h);
    }

    h.left = deleteMin(h.left);
    return balance(h);
  }

  // PRE: h为红色, h.left, h.left.left为黑色
  // POST: h.left或者其子节点置为红色
  private Node moveRedLeft(Node h) {

    this.flipColor(h);
    if (this.isRed(h.right.left)) {
      h.right = this.rotateRight(h.right);
      h = this.rotateLeft(h);
      this.flipColor(h);
    }
    return h;
  }

  // PRE: h为红色, h.right, h.right.left为黑色
  // POST: h.right或者其子节点置为红色
  private Node moveRedRight(Node h) {
    this.flipColor(h);
    if (this.isRed(h.left.left)) {
      h = this.rotateRight(h);
      this.flipColor(h);
    }
    return h;
  }

  // 保持红黑树的性质
  private Node balance(Node h) {

    // 红右链接
    if (this.isRed(h.right)) {
      h = this.rotateLeft(h);
    }
    // 连续的红左链接
    if (this.isRed(h.left) && this.isRed(h.left.left)) {
      h = this.rotateRight(h);
    }
    // 左右子联接均为红色
    if (this.isRed(h.left) && this.isRed(h.right)) {
      this.flipColor(h);
    }

    h.N = size(h.left) + size(h.right) + 1;
    return h;
  }

  @Override
  public void deleteMax() {
    Preconditions.checkState(!this.isEmpty(), "empty!");

    // 左右子链接均为黑色, 将根节点置为红色
    if (!this.isRed(root.left) && !this.isRed(root.right)) {
      root.color = RED;
    }

    root = this.deleteMax(root);
    if (!this.isEmpty()) {
      root.color = BLACK;
    }
  }

  private Node deleteMax(Node h) {
    if (this.isRed(h.left)) {
      h = this.rotateRight(h);
    }

    if (h.right == null) {
      return null;
    }

    if (!this.isRed(h.right) && !this.isRed(h.right.left)) {
      h = moveRedRight(h);
    }

    h.right = this.deleteMax(h.right);

    return this.balance(h);
  }

  @Override
  public int size(Key low, Key high) {
    Preconditions.checkArgument(low != null, "null low!");
    Preconditions.checkArgument(high != null, "null high!");

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
    Preconditions.checkArgument(low != null, "null low!");
    Preconditions.checkArgument(high != null, "null high!");

    Queue<Key> queue = new Queue<>();
    this.keys(root, queue, low, high);
    return queue;
  }

  private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
    if (x == null) {
      return;
    }
    int cmplo = lo.compareTo(x.key);
    int cmphi = hi.compareTo(x.key);
    if (cmplo < 0) {
      this.keys(x.left, queue, lo, hi);
    }
    if (cmplo <= 0 && cmphi >= 0) {
      queue.enqueue(x.key);
    }
    if (cmphi > 0) {
      this.keys(x.right, queue, lo, hi);
    }
  }

  @Override
  public Iterable<Key> keys() {
    if (this.isEmpty()) {
      return new Queue<>();
    }
    return this.keys(this.min(), this.max());
  }

  // ======================================== 辅助方法
  // 是否为红链入节点
  private boolean isRed(Node x) {
    if (x == null) {
      return false;
    }
    return x.color == RED;
  }

  // 左旋h的右链接, 返回旋转后的根节点
  private Node rotateLeft(Node h) {
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;
    x.N = h.N;
    h.N = 1 + this.size(h.left) + this.size(h.right);
    return x;
  }

  private int size(Node x) {
    if (x == null) {
      return 0;
    }
    return x.N;
  }

  // 右旋h的左链接, 返回旋转后的根节点
  private Node rotateRight(Node h) {
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;
    x.N = h.N;
    h.N = 1 + this.size(h.left) + this.size(h.right);
    return x;
  }

  public static void main(String[] args) {
    RedBlackBST<String, Integer> rb = new RedBlackBST<>();
    int i = 0;
    for (String key : "S E A R C H E X A M P L E".split("\\s")) {
      rb.put(key, i);
      i++;
    }
    rb.dump();

    System.out.println();
    rb.delete("E");
    rb.delete("M");
    rb.dump();
  }

  public void dump() {
    this.print(root);
  }

  private void print(Node x) {
    if (x == null) {
      return;
    }

    // 中序遍历
    this.print(x.left);
    System.out.println(x.key + "(" + x.val + ")");
    this.print(x.right);
  }
}
