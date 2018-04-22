package com.spike.giantdataanalysis.model.algorithms.graph.core;

import com.spike.giantdataanalysis.model.algorithms.adt.Bag;

/**
 * 有向图.
 * @author zhoujiagen
 */
public class DirectedGraph implements IGraph {

  private final int V; // 顶点的数量
  private int E; // 边的数量
  private Bag<Integer>[] adj;// 邻接表

  /** 构造有向图. */
  @SuppressWarnings("unchecked")
  public DirectedGraph(int V) {
    this.V = V;
    this.E = 0;
    this.adj = (Bag<Integer>[]) new Bag[V];
    for (int v = 0; v < V; v++) {
      adj[v] = new Bag<Integer>();
    }
  }

  /** 顶点的数量. */
  @Override
  public int V() {
    return V;
  }

  /** 边的数量. */
  @Override
  public int E() {
    return E;
  }

  /** 添加由v指向w的边. */
  @Override
  public void addEdge(int v, int w) {
    adj[v].add(w);
    E++;
  }

  /** 由顶点v指出的边另一端顶点. */
  @Override
  public Iterable<Integer> adj(int v) {
    return adj[v];
  }

  /** 图的逆. */
  public DirectedGraph reverse() {
    DirectedGraph R = new DirectedGraph(V);

    for (int v = 0; v < V; v++) {
      for (int w : this.adj(v)) {
        R.addEdge(w, v); // 逆向的边
      }
    }

    return R;
  }

}
