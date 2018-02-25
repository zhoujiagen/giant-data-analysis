package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 带权重边的图接口.
 * @author zhoujiagen
 */
public interface IWeightedGraph {

  /** 顶点数量. */
  int V();

  /** 边数量. */
  int E();

  /** 添加由顶点v和w构成的边. */
  void addEdge(Edge edge);

  /** 获取顶点v的邻接顶点. */
  Iterable<Edge> adj(int v);

  /** 图中所有边. */
  Iterable<Edge> edges();
}
