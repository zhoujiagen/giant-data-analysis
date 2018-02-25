package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 图中单点路径.
 * @author zhoujiagen
 */
public interface IPaths<GRAPH extends IGraph> {
  /** 无向图或无向图G. */
  GRAPH G();

  /** 搜索开始顶点s. */
  int s();

  /** 是否存在从开始顶点s到顶点v的路径. */
  boolean hasPathTo(int v);

  /** 开始顶点s到顶点v的路径, 不存在返回null. */
  Iterable<Integer> pathTo(int v);
}
