package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 图的抽象接口: 无向图Graph, 有向图DirectedGraph.
 * @author zhoujiagen
 */
public interface IGraph {

  /** 顶点数量. */
  int V();

  /** 边数量. */
  int E();

  /** 添加由顶点v和w构成的边. */
  void addEdge(int v, int w);

  /** 获取顶点v的邻接顶点. */
  Iterable<Integer> adj(int v);
}
