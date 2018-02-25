package com.spike.giantdataanalysis.model.algorithms.graph.cycle;

import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IDFS.IDFSWithFather;

/**
 * 无向图中环检测: 使用深度优先搜索.
 * @author zhoujiagen
 */
public class CycleDetection implements IDFSWithFather<Graph> {
  private boolean[] marked; // 顶点是否访问过, 索引: 顶点
  private boolean hasCycle = false; // 是否有环标志

  public CycleDetection(Graph G) {
    this.marked = new boolean[G.V()];
    for (int s = 0; s < G.V(); s++) {
      if (!marked[s]) {
        this.dfs(G, s, s);
      }
    }
  }

  // 通过顶点u访问其邻接顶点v的深度优先搜索
  @Override
  public void dfs(Graph G, int v, int u) {
    marked[v] = true;
    for (int w : G.adj(v)) {
      if (!marked[w]) {
        this.dfs(G, w, v);
      } else if (w != u) {
        // w已访问过, 构成环(u->v->w->u)
        hasCycle = true;
      }
    }
  }

  public boolean hasCycle() {
    return hasCycle;
  }
}
