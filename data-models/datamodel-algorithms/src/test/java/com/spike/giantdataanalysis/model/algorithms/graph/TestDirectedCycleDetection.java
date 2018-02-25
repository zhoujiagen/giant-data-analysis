package com.spike.giantdataanalysis.model.algorithms.graph;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.graph.core.DirectedGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.cycle.DirectedCycleDetection;

public class TestDirectedCycleDetection {

  @Test
  public void example() {
    int[] vs = new int[] { 0, 1, 2, 3, 4, 5 };
    DirectedGraph DG = new DirectedGraph(vs.length);

    DG.addEdge(0, 5);
    DG.addEdge(3, 5);
    DG.addEdge(5, 4);
    DG.addEdge(4, 3);

    DirectedCycleDetection dcd = new DirectedCycleDetection(DG);
    if (dcd.hasCycle()) {
      for (int v : dcd.cycle()) {
        System.out.print(v + " ");
      }
      System.out.println();
    }

    // OUT
    // 3 5 4 3
  }
}
