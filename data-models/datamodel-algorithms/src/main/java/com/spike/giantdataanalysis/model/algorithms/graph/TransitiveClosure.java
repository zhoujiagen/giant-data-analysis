package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;

/**
 * 有向图中的传递闭包.
 * @author zhoujiagen
 */
public class TransitiveClosure {

  private DirectedDFS[] all; // 在所有顶点上执行DFS

  public TransitiveClosure(DirectedGraph DG) {
    all = new DirectedDFS[DG.V()];
    for (int v = 0; v < DG.V(); v++) {
      all[v] = new DirectedDFS(DG, v);
    }
  }

  /** 顶点v可否可达顶点w. */
  public boolean reachable(int v, int w) {
    return all[v].marked(w);
  }
}
