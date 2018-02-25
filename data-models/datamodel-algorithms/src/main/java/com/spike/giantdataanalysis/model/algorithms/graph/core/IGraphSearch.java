package com.spike.giantdataanalysis.model.algorithms.graph.core;

/**
 * 图中单点搜索.
 * @author zhoujiagen
 */
public interface IGraphSearch {
  /** 无向图G. */
  Graph G();

  /** 搜索开始顶点s. */
  int s();

  /** 顶点v是否与开始顶点s连通. */
  boolean marked(int v);

  /** 与开始顶点s连通的顶点总数. */
  int count();
}
