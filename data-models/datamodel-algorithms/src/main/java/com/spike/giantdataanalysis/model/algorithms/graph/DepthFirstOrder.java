package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.adt.Stack;
import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IDFS;

/**
 * 深度优先顺序: 深度优先搜索中递归调用的顺序.
 * <p>
 * 用于拓扑排序和强连通性.
 * @author zhoujiagen
 */
public class DepthFirstOrder implements IDFS<DirectedGraph> {
  private boolean[] marked; // 顶点是否访问过, 索引: 顶点

  private Queue<Integer> pre; // 先序
  private Queue<Integer> post; // 后序
  private Stack<Integer> reversePost; // 逆后序

  public DepthFirstOrder(DirectedGraph DG) {
    this.marked = new boolean[DG.V()];

    this.pre = new Queue<>();
    this.post = new Queue<>();
    this.reversePost = new Stack<>();

    for (int v = 0; v < DG.V(); v++) {
      if (!marked[v]) {
        this.dfs(DG, v);
      }
    }
  }

  @Override
  public void dfs(DirectedGraph DG, int v) {
    pre.enqueue(v); // 入栈: 先序

    marked[v] = true;
    for (int w : DG.adj(v)) {
      if (!marked[w]) {
        this.dfs(DG, w);
      }
    }

    post.enqueue(v); // 出栈: 后序
    reversePost.push(v);// 出栈: 逆后序
  }

  public Queue<Integer> pre() {
    return pre;
  }

  public Queue<Integer> post() {
    return post;
  }

  public Stack<Integer> reversePost() {
    return reversePost;
  }

}