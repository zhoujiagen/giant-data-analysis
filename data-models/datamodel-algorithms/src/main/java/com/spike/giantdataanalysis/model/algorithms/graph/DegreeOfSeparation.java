package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;

/**
 * 顶点间距离度量(最短路径).
 * <p>
 * 使用广度优先搜索.
 * @author zhoujiagen
 */
public class DegreeOfSeparation {

  public static void main(String[] args) {
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
    int t = 4;
    BreadthFirstPaths<Graph> bfs = new BreadthFirstPaths<Graph>(G, s);
    boolean hasPath = bfs.hasPathTo(t);
    if (hasPath) {
      for (int v : bfs.pathTo(t)) {
        System.out.println(v);
      }
    }
  }

}
