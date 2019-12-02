package com.spike.giantdataanalysis.model.algorithms.search;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.commons.annotation.constraint.InvariantConstraint;
import com.spike.giantdataanalysis.commons.annotation.constraint.runtime.Parameter;
import com.spike.giantdataanalysis.model.algorithms.adt.Queue;

/**
 * 二叉查找树.
 * <p>
 * 实现说明: 沿着根节点遍历左右子树, 执行递归操作.
 * <p>
 * 性质和实现:
 * 
 * <pre>
 * (1) 键
 * 一颗二叉树, 没有节点都含有一个Comparable的键及其值;
 * 每个节点的键大于其左子树中的任意节点的键, 小于右子树的任意节点的键.
 * 
 * (2) 查找[插入]: 在以当前节点为根的子树中
 * 当前节点为null, 未命中[新创建节点].
 * 查找键小于当前节点键, 在左子树中查找[插入];
 * 查找键大于当前节点键, 在右子树中查找[插入];
 * 查找键等于当前节点键, 命中[更新值].
 * 
 * (3) 删除
 * 待删除节点x
 * 左右子树节点有一个为null: 以左子节点为null为例, 将树中原指向x的引用指向x.right
 * 左右子树节点均不为null: 用T. Hibbard的方法使用x的后继节点替换x, 后继节点为x的右子树中的最小键节点.
 * 
 * (4) 范围查找
 * 使用中序遍历, 保持遍历结果有序.
 * </pre>
 * @author zhoujiagen
 */
@InvariantConstraint(description = "每个节点的键大于其左子树中的任意节点的键, 小于右子树的任意节点的键.")
public class BST<Key extends Comparable<Key>, Value> implements IOrderedST<Key, Value> {

  private class Node {
    private Key key; // 键
    private Value val; // 值
    private Node left, right; // 左右子树链接
    @InvariantConstraint(description = "size(x) = size(x.left) + size(x.right) + 1")
    private int N = 0; // 以该节点为根的子树中的节点总数

    public Node(Key key, Value val, int N) {
      this.key = key;
      this.val = val;
      this.N = N;
    }

    @Override
    public String toString() {
      return "Node [key=" + key + ", val=" + val + ", left=" + left + ", right=" + right + ", N="
          + N + "]";
    }
  }

  private Node root; // 二叉查找树的根节点

  @Override
  public void put(Key key, Value val) {
    Preconditions.checkArgument(key != null, "null argument key!");

    if (val == null) {
      this.delete(key);
      return;
    }

    root = this.put(root, key, val);
  }

  // 在以当前节点x为根的子树中插入键值对
  // key在子树中存在, 则更新其值; 否则在子树中创建新节点
  // x为null时, 返回新创建的节点, 否则返回当前节点x
  private Node put(Node x, Key key, Value val) {
    if (x == null) {
      return new Node(key, val, 1);
    }

    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      // 在左子树中插入
      x.left = this.put(x.left, key, val);
    } else if (cmp > 0) {
      // 在右子树中插入
      x.right = this.put(x.right, key, val);
    } else {
      x.val = val;
    }

    x.N = this.size(x.left) + this.size(x.right) + 1;
    return x;
  }

  @Override
  public Value get(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    return this.get(root, key);
  }

  // 在以节点x为根的树中查找key
  private Value get(Node x, Key key) {
    if (x == null) {
      return null;
    }

    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      // 在左子树中查找
      return this.get(x.left, key);
    } else if (cmp > 0) {
      // 在右子树中查找
      return this.get(x.right, key);
    } else {
      // 命中
      return x.val;
    }
  }

  @Override
  public void delete(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");
    Preconditions.checkState(!this.isEmpty(), "empty tree!");

    root = this.delete(root, key);
  }

  /**
   * 在当前节点x为根的子树中删除键key对应的节点, 返回删除后的当前子树根节点x的左右子节点或后继节点.
   * 
   * <pre>
   * 待删除节点x有左右子节点时, 使用T. Hibbard的后继节点替换待删除节点x方法:
   * (1) 将t指向待删除节点x
   * (2) 将x指向x的右子树中最小键节点
   * (3) 将x.right指向在t的右子树中删除最小键节点后的根节点
   * (4) 将x.left指向t.left, 完成替换.
   * </pre>
   * @param x
   * @param key
   * @return
   */
  private Node delete(Node x, Key key) {
    if (x == null) {
      return null;
    }

    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      // 在左子树中删除
      x.left = this.delete(x.left, key);
    } else if (cmp > 0) {
      // 在右子树中删除
      x.right = this.delete(x.right, key);
    } else {
      // 删除当前节点
      if (x.left == null) {
        return x.right;
      } else if (x.right == null) {
        return x.left;
      } else {
        // 当前节点有左右子树
        Node t = x;
        x = this.min(x.right);
        x.right = this.deleteMin(t.right);
        x.left = t.left;
      }
    }
    x.N = this.size(x.left) + this.size(x.right) + 1;
    return x;
  }

  @Override
  public boolean contains(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    return this.get(key) != null;
  }

  @Override
  public boolean isEmpty() {
    return this.size() == 0;
  }

  @Override
  public int size() {
    return this.size(root);
  }

  // 以节点为根的子树中节点的数量
  private int size(Node x) {
    if (x == null) {
      return 0;
    } else {
      return x.N;
    }
  }

  @Override
  public Key min() {
    return this.min(root).key;
  }

  // 在以当前节点x为根的子树中查找有最小键的节点
  private Node min(Node x) {
    if (x.left == null) {
      // 左子树为null, 当前节点有最小键
      return x;
    } else {
      // 在左子树中查找
      return this.min(x.left);
    }
  }

  @Override
  public Key max() {
    return this.max(root).key;
  }

  /** @see #min(Node) */
  private Node max(Node x) {
    if (x.right == null) {
      return x;
    } else {
      return this.max(x.right);
    }
  }

  @Override
  public Key floor(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    Node x = this.floor(root, key);
    if (x == null) {
      return null;
    } else {
      return x.key;
    }
  }

  // 在以当前节点x为根的子树中查找键<=key的节点
  // key=x.key: 命中;
  // key<x.key: 小于等于key的最大键一定在左子树中;
  // key>x.key: 右子树中存在键小于等于key的节点时, 小于等于key的最大键在右子树中; 否则当前节点是小于等于key的最大键节点
  private Node floor(Node x, Key key) {
    if (x == null) return null;

    int cmp = key.compareTo(x.key);
    if (cmp == 0) {
      // 命中
      return x;
    } else if (cmp < 0) {
      // 在左子树中查找
      return this.floor(x.left, key);
    } else {
      // 在右子树中查找
      Node t = this.floor(x.right, key);
      if (t != null) {
        // 存在键<=key的节点
        return t;
      } else {
        return x;
      }
    }
  }

  @Override
  public Key ceiling(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    Node x = this.ceiling(root, key);
    if (x == null) {
      return null;
    } else {
      return x.key;
    }
  }

  // 在以当前节点x为根的子树中查找键>=key的节点
  private Node ceiling(Node x, Key key) {
    if (x == null) return null;

    int cmp = key.compareTo(x.key);
    if (cmp == 0) {
      // 命中
      return x;
    } else if (cmp > 0) {
      // 在右子树中查找
      return this.ceiling(x.right, key);
    } else {
      // 在左子树中查找
      Node t = this.ceiling(x.left, key);
      if (t != null) {
        // 存在键>=key的节点
        return t;
      } else {
        return x;
      }
    }

  }

  @Override
  public int rank(Key key) {
    Preconditions.checkArgument(key != null, "null argument!");

    return this.rank(root, key);
  }

  // 在以当前节点x为根节点的子树中, 计算<key的键的数量
  private int rank(Node x, Key key) {
    if (x == null) return 0;

    int cmp = key.compareTo(x.key);
    if (cmp < 0) {
      // key小于当前节点的键: rank为key在左子树中的rank
      return this.rank(x.left, key);
    } else if (cmp > 0) {
      // key大于当前节点的键: rank为左子树节点数量+1(当前节点)+key在右子树中的rank
      return 1 + this.size(x.left) + this.rank(x.right, key);
    } else {
      // key等于当前节点的键: rank为左子树中节点数量
      return this.size(x.left);
    }
  }

  @Override
  public Key select(int k) {
    Preconditions.checkArgument(k > 0 && k < this.size(), "invalid argument");

    Node x = this.select(root, k);
    if (x == null) {
      return null;
    } else {
      return x.key;
    }
  }

  // 在以当前节点x为根的子树中查找排名为k的节点
  private Node select(Node x, int k) {
    if (x == null) {
      return null;
    }

    int t = this.size(x.left);// 左子树中节点数量

    if (t > k) {
      // 左子树中节点数大于k: 在左子树中查找
      return this.select(x.left, k);
    } else if (t < k) {
      // 左子树中节点数小于k: 在右子树中查找减去左子树和当前节点数量后的排名
      return this.select(x.right, k - (t + 1));
    } else {
      // 左子树中节点数等于k: 当前节点即为结果
      return x;
    }
  }

  @Override
  public void deleteMin() {
    Preconditions.checkState(!this.isEmpty(), "empty tree!");

    root = this.deleteMin(root);
  }

  // 在以当前节点x为根节点的子树中删除最小键节点
  // 找到最小键节点时(即遇到null左链接), 返回该节点的右链接
  // 未找到最小键节点时, 在左子树中递归调用
  private Node deleteMin(Node x) {
    if (x.left == null) {
      // 找到最小键节点
      return x.right;
    }

    // 在左子树中删除
    x.left = this.deleteMin(x.left);
    x.N = this.size(x.left) + this.size(x.right) + 1; // 更新子树中节点数量
    return x;
  }

  @Override
  public void deleteMax() {
    Preconditions.checkState(!this.isEmpty(), "empty tree!");

    root = this.deleteMax(root);
  }

  // 在以当前节点x为根节点的子树中删除最大键节点
  private Node deleteMax(Node x) {
    if (x.right == null) {
      return x.left;
    }

    x.right = this.deleteMax(x.right);
    x.N = this.size(x.left) + this.size(x.right) + 1;
    return x;
  }

  @Override
  public int size(Key low, Key high) {
    Preconditions.checkArgument(low != null, "null argument low!");
    Preconditions.checkArgument(high != null, "null argument high!");

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
  public Iterable<Key> keys() {
    return this.keys(this.min(), this.max());
  }

  @Override
  public Iterable<Key> keys(Key low, Key high) {
    Preconditions.checkArgument(!(low == null && high == null), "all null arguments!");
    Preconditions.checkArgument(low.compareTo(high) <= 0, "invalid argument!");

    Queue<Key> q = new Queue<>();
    this.keys(root, q, low, high);
    return q;
  }

  // 在以当前节点x为根节点的子树中查找键在[low, high]范围内的节点, 加入队列q中
  // 使用中序遍历
  private void keys(Node x, @Parameter.OUT Queue<Key> q, Key low, Key high) {
    if (x == null) {
      return;
    }

    // 使用中序遍历: 左子树->当前节点->右子树
    int cmpLow = low.compareTo(x.key);
    int cmpHigh = high.compareTo(x.key);
    if (cmpLow < 0) {
      // 左子树中可能有>low的键节点
      this.keys(x.left, q, low, high);
    }
    if (cmpLow <= 0 && cmpHigh >= 0) {
      // 当前节点键在范围内: 加入队列中
      q.enqueue(x.key);
    }
    if (cmpHigh > 0) {
      // 右子树中可能有<high的键节点
      this.keys(x.right, q, low, high);
    }
  }

  public void dump() {
    this.print(root);
  }

  public void dumpTree() {
    System.out.println(root);
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

  public void dumpGraphviz() {
    List<Node> outNodes = Lists.newArrayList();
    List<Edge> outEdges = Lists.newArrayList();
    this.dumpGraphviz(root, outNodes, outEdges);

    StringBuilder sb = new StringBuilder();
    sb.append("digraph " + this.getClass().getSimpleName() + " {\n");
    sb.append("\t").append("size=100; ratio=0.8; splines=polyline\n");
    sb.append("\t").append(
      "node [shape = \"record\", fixedsize=true, fontsize=11, width=.3, height=.3]\n");
    sb.append("\t").append("edge [arrowsize=0.5, minlen=1]\n\n");

    for (Node x : outNodes) {
      sb.append("\t").append(x.key).append("[label=\"{<f1> " + x.key + "| {<f0> | <f2>}}\"]\n");
    }
    sb.append("\n");
    for (Edge e : outEdges) {
      if (Direction.LEFT.equals(e.direction)) {
        sb.append("\t").append(e.source.key + ":f0 -> " + e.target.key + ":f1\n");
      } else if (Direction.RIGHT.equals(e.direction)) {
        sb.append("\t").append(e.source.key + ":f2 -> " + e.target.key + ":f1\n");
      }
    }

    sb.append("}");

    System.out.println(sb.toString());
  }

  private enum Direction {
    LEFT, RIGHT
  }

  // 用于dumpGraphviz
  private class Edge {
    Node source;
    Node target;
    Direction direction;

    Edge(Node source, Node target, Direction direction) {
      this.source = source;
      this.target = target;
      this.direction = direction;
    }
  }

  private void dumpGraphviz(Node x, List<Node> outNodes, List<Edge> outEdges) {
    if (x == null) {
      return;
    }

    dumpGraphviz(x.left, outNodes, outEdges);

    outNodes.add(x);
    if (x.left != null) {
      outEdges.add(new Edge(x, x.left, Direction.LEFT));
    }
    if (x.right != null) {
      outEdges.add(new Edge(x, x.right, Direction.RIGHT));
    }

    dumpGraphviz(x.right, outNodes, outEdges);
  }

  public static void main(String[] args) {

    BST<String, Integer> bst = new BST<>();
    int i = 0;
    for (String key : "S E A R C H E X A M P L E".split("\\s")) {
      bst.put(key, i);
      i++;
    }

    bst.dump();
    bst.dumpTree();
    bst.dumpGraphviz(); // https://stamm-wilbrandt.de/GraphvizFiddle/

    // System.out.println(Joiner.on(" ").join(bst.keys()).toString());
    for (String key : bst.keys()) {
      System.out.println(key + " " + bst.get(key));
    }

  }
}
