package com.spike.giantdataanalysis.model.algorithms.graph;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;

public class TestTopologicalSort {

  @Test
  public void example() {
    int[] vs = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    DirectedGraph DG = new DirectedGraph(vs.length);

    DG.addEdge(0, 1);
    DG.addEdge(0, 5);
    DG.addEdge(0, 6);
    DG.addEdge(2, 0);
    DG.addEdge(2, 3);
    DG.addEdge(3, 5);
    DG.addEdge(5, 4);
    DG.addEdge(6, 4);
    DG.addEdge(6, 9);
    DG.addEdge(7, 6);
    DG.addEdge(8, 7);
    DG.addEdge(9, 10);
    DG.addEdge(9, 11);
    DG.addEdge(9, 12);
    DG.addEdge(11, 12);

    TopologicalSort ts = new TopologicalSort(DG);
    if (ts.isDAG()) {
      for (int v : ts.order()) {
        System.out.print(v + " ");
      }
      System.out.println();
    }

    // OUT
    // 8 7 2 3 0 1 5 6 4 9 10 11 12
  }
}
