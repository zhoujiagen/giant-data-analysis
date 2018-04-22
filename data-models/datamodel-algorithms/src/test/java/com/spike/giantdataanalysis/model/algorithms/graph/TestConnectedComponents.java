package com.spike.giantdataanalysis.model.algorithms.graph;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.graph.cc.ConnectedComponents;
import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;

public class TestConnectedComponents {

  @Test
  public void dump() {
    int[] vs = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    Graph G = new Graph(vs.length);
    G.addEdge(0, 5);
    G.addEdge(4, 3);
    G.addEdge(0, 1);
    G.addEdge(9, 12);
    G.addEdge(6, 4);
    G.addEdge(5, 4);
    G.addEdge(0, 2);
    G.addEdge(11, 12);
    G.addEdge(9, 10);
    G.addEdge(0, 6);
    G.addEdge(7, 8);
    G.addEdge(9, 11);
    G.addEdge(5, 3);

    // G.dumpGraphviz();

    ConnectedComponents cc = new ConnectedComponents(G);
    cc.dump();

  }
}
