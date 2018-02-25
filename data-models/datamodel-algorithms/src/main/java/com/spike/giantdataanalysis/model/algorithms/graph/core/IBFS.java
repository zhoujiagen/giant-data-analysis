package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 广度优先搜索接口.
 * @author zhoujiagen
 */
public interface IBFS<GRAPH extends IGraph> {
  void bfs(GRAPH G, int s);
}
