package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IDFS;

/**
 * 双色问题: 给顶点着两种颜色, 是否存在着色方案使得没有边两端的顶点同色.
 * <p>
 * 使用深度优先搜索.
 * @author zhoujiagen
 */
public class TwoColorable implements IDFS<Graph> {
  private boolean[] marked; // 顶点是否访问过, 索引: 顶点
  private boolean[] color; // 顶点着色, 索引: 顶点
  private boolean isTwoColorable = true; // 是否可着双色标志

  public TwoColorable(Graph G) {
    this.marked = new boolean[G.V()];
    this.color = new boolean[G.V()];
    for (int s = 0; s < G.V(); s++) {
      if (!marked[s]) {
        this.dfs(G, s);
      }
    }
  }

  @Override
  public void dfs(Graph G, int v) {
    marked[v] = true;
    for (int w : G.adj(v)) {
      if (!marked[w]) {
        color[w] = !color[v]; // 反色
        this.dfs(G, w);
      } else if (color[w] == color[v]) {
        // 顶点w已访问过, 颜色相同
        isTwoColorable = false;
      }
    }
  }

  public boolean isTwoColorable() {
    return isTwoColorable;
  }

}
