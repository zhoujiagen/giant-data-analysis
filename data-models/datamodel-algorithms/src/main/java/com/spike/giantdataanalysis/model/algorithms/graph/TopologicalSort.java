package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.cycle.DirectedCycleDetection;

/**
 * 有向图上的拓扑排序, 仅是有向无环图(DAG)时可排序.
 * @author zhoujiagen
 */
public class TopologicalSort {

  private Iterable<Integer> order; // 顶点排序

  public TopologicalSort(DirectedGraph DG) {
    DirectedCycleDetection dcd = new DirectedCycleDetection(DG);
    if (!dcd.hasCycle()) {
      DepthFirstOrder dfo = new DepthFirstOrder(DG);
      order = dfo.reversePost(); // 逆后序的方法
    }
  }

  /** 是否是有向无环图. */
  public boolean isDAG() {
    return order != null;
  }

  /** 返回排序结果, 不可排序时返回null. */
  public Iterable<Integer> order() {
    return order;
  }
}
