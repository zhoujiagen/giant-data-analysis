package com.spike.giantdataanalysis.model.algorithms.graph;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;

public class TestBreadthFirstPaths {

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

    int s = 0;
    BreadthFirstPaths<Graph> bfs = new BreadthFirstPaths<Graph>(G, s);
    System.out.println(bfs);
    for (int v = 0; v < G.V(); v++) {
      System.out.print(s + " to " + v + ": ");
      if (bfs.hasPathTo(v)) {

        for (int x : bfs.pathTo(v)) {
          if (x == s) {
            System.out.print(x);
          } else {
            System.out.print("-" + x);
          }
        }
        System.out.println();
      }
    }
  }
}
