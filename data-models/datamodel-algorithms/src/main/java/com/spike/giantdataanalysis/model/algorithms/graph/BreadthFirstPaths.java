package com.spike.giantdataanalysis.model.algorithms.graph;

import java.util.Arrays;

import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.adt.Stack;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IBFS;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IGraph;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IPaths;

/**
 * 使用广度优先搜索查找图中路径. 适用于无向图和有向图.
 * 
 * <pre>
 * 要找到从开始顶点s到顶点v的最短路径: 
 * 从s开始, 在所有由一条边可以到达的顶点中找v, 如果没有找到, 在距s两条边的所有顶点中找v, 依此一直执行.
 * 
 * 实现:
 * 使用FIFO队列保存所有已经标记过的但其邻接顶点未被检查过的顶点;
 * 将开始顶点s加入队列, 重复执行直到队列为空: (1) 取队列中下一个顶点v并标记它, (2) 将v的所有未标记的邻接顶点加入队列.
 * </pre>
 * @author zhoujiagen
 */
public class BreadthFirstPaths<GRAPH extends IGraph> implements IPaths<GRAPH>, IBFS<GRAPH> {

  private final GRAPH G;
  private final int s; // 开始顶点

  private boolean[] marked; // 顶点是否访问过, 索引: 顶点
  private int[] edgeTo; // 从开始顶点s到一个顶点的路径上的最后一个顶点(父顶点), 索引: 顶点, 值: 父顶点
  private int[] distTo; // 从开始顶点s到一个顶点的最短路径长度, 索引: 顶点, 值: 长度

  public BreadthFirstPaths(GRAPH G, int s) {
    this.G = G;
    this.s = s;
    this.marked = new boolean[G.V()];
    this.edgeTo = new int[G.V()];
    this.distTo = new int[G.V()];
    for (int v = 0; v < G.V(); v++) {
      distTo[v] = Integer.MAX_VALUE;
    }

    this.bfs(G, s);
  }

  @Override
  public void bfs(GRAPH G, int s) {
    Queue<Integer> q = new Queue<>();
    marked[s] = true;
    distTo[s] = 0;
    q.enqueue(s);

    while (!q.isEmpty()) {
      int v = q.dequeue(); // 出队
      // System.err.println("DEBUG>>> dequeue " + v + ", Queue=" + q);

      for (int w : G.adj(v)) {
        if (!marked[w]) {
          edgeTo[w] = v; // 记录路径中父节点
          distTo[w] = distTo[v] + 1;
          marked[w] = true;
          q.enqueue(w); // 入队
        }
      }
    }
  }

  @Override
  public GRAPH G() {
    return G;
  }

  @Override
  public int s() {
    return s;
  }

  @Override
  public boolean hasPathTo(int v) {
    return marked[v];
  }

  @Override
  public Iterable<Integer> pathTo(int v) {
    if (!this.hasPathTo(v)) {
      return null;
    }

    Stack<Integer> stack = new Stack<>();
    for (int w = v; w != s; w = edgeTo[w]) {
      stack.push(w);
    }
    stack.push(s);
    return stack;
  }

  public int distTo(int v) {
    return distTo[v];
  }

  @Override
  public String toString() {
    return "BreadthFirstPaths [s=" + s + ",\n marked=" + Arrays.toString(marked) + ",\n edgeTo="
        + Arrays.toString(edgeTo) + ",\n distTo=" + Arrays.toString(distTo) + "]";
  }

}
