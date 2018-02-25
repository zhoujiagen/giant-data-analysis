package com.spike.giantdataanalysis.model.algorithms.graph.cc;

import com.spike.giantdataanalysis.model.algorithms.adt.Bag;
import com.spike.giantdataanalysis.model.algorithms.graph.DepthFirstOrder;
import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IDFS;

/**
 * 有向图中的强连通性.
 * 
 * <pre>
 * Kosaraju算法的步骤:
 * 
 * (1) 有向图DG, 计算其逆图DGR的逆后序;
 * (2) 以(1)中顶点顺序在DG上执行DFS;
 * (3) 递归调用中可达的顶点在同一个强连通分量中.
 * </pre>
 * @author zhoujiagen
 * @see ConnectedComponents
 */
public class StrongConnectedComponents implements IDFS<DirectedGraph> {
  private final DirectedGraph DG;
  private boolean[] marked;// 顶点是否访问过, 索引: 顶点
  private int[] id; // 顶点与强连通分量标识对应关系, 索引: 顶点, 值: 连通分量标识
  private int count; // 强连通分量的数量

  public StrongConnectedComponents(DirectedGraph DG) {
    this.DG = DG;
    this.marked = new boolean[DG.V()];
    this.id = new int[DG.V()];
    DepthFirstOrder order = new DepthFirstOrder(DG);
    for (int s : order.reversePost()) { // 按逆后序调用DFS
      if (!marked[s]) {
        this.dfs(DG, s);
        count++; // 强连通分量计数
      }
    }
  }

  @Override
  public void dfs(DirectedGraph DG, int v) {
    marked[v] = true;
    id[v] = count; // 递归调用中的顶点在同一强连通分量中

    for (int w : DG.adj(v)) {
      if (!marked[w]) {
        this.dfs(DG, w);
      }
    }
  }

  /** 顶点v和w是否连通. */
  public boolean connected(int v, int w) {
    return id[v] == id[w];
  }

  /** 图中连通分量的数量. */
  public int count() {
    return count;
  }

  /** 顶点v所在的连通分量编号(0 ~ count()-1). */
  public int id(int v) {
    return id[v];
  }

  public void dump() {
    @SuppressWarnings("unchecked")
    Bag<Integer>[] ccs = (Bag<Integer>[]) new Bag[count];
    for (int cc = 0; cc < count; cc++) {
      ccs[cc] = new Bag<>();
    }
    for (int v = 0; v < DG.V(); v++) {
      ccs[this.id(v)].add(v);
    }
    for (int cc = 0; cc < count; cc++) {
      System.out.println("SCC[" + cc + "]: " + ccs[cc]);
    }
  }

}
