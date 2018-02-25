package com.spike.giantdataanalysis.model.algorithms.graph;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;

public class TestGraph {

  @Test
  public void example() {
    int[] vs = new int[] { 0, 1, 2, 3, 4, 5 };
    Graph G = new Graph(vs.length);

    G.addEdge(0, 2);
    G.addEdge(0, 1);
    G.addEdge(0, 5);
    G.addEdge(1, 2);
    G.addEdge(2, 3);
    G.addEdge(2, 4);
    G.addEdge(3, 4);
    G.addEdge(3, 5);

    // G.dumpAdjGraphviz();
    G.dumpGraphviz();
  }
}
