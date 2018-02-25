package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 深度优先搜索接口.
 * @author zhoujiagen
 */
public interface IDFS<GRAPH extends IGraph> {

  void dfs(GRAPH G, int v);

  /** 带父顶点的深度优先搜索接口. */
  interface IDFSWithFather<GRAPH extends IGraph> {
    void dfs(GRAPH G, int v, int fatherOfv);
  }
}
