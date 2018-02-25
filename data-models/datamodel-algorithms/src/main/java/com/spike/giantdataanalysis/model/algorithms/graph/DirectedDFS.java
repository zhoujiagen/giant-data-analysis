package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IDFS;

/**
 * 有向图中的深度优先搜索, 确定可达性.
 * @author zhoujiagen
 */
public class DirectedDFS implements IDFS<DirectedGraph> {

  private final DirectedGraph DG; // 有向图
  private final int s; // 开始顶点
  private boolean[] marked; // 顶点是否访问过, 索引: 顶点

  public DirectedDFS(DirectedGraph DG, int s) {
    this.DG = DG;
    this.s = s;
    this.marked = new boolean[DG.V()];

    this.dfs(this.DG, this.s);
  }

  @Override
  public void dfs(DirectedGraph DG, int v) {
    marked[v] = true;

    for (int w : DG.adj(v)) {
      if (!marked[w]) {
        this.dfs(DG, w);
      }
    }
  }

  public boolean marked(int v) {
    return marked[v];
  }
}
