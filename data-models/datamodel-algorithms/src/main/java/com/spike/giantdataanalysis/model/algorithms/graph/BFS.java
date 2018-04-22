package com.spike.giantdataanalysis.model.algorithms.graph;

import com.spike.giantdataanalysis.model.algorithms.adt.Queue;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IBFS;
import com.spike.giantdataanalysis.model.algorithms.graph.core.IGraph;

/**
 * 广度优先搜索((Breadth First Search, BFS).
 * 
 * <pre>
 * 实现:
 * 
 * 使用FIFO队列保存所有已经标记过的但其邻接顶点未被检查过的顶点;
 * 将开始顶点s加入队列, 重复执行直到队列为空: (1) 取队列中下一个顶点v并标记它, (2) 将v的所有未标记的邻接顶点加入队列.
 * </pre>
 * @author zhoujiagen
 */
public class BFS<GRAPH extends IGraph> implements IBFS<GRAPH> {

  private boolean[] marked; // 顶点是否访问过, 索引: 顶点

  public BFS(GRAPH G, int s) {
    this.marked = new boolean[G.V()];

    this.bfs(G, s);
  }

  @Override
  public void bfs(GRAPH G, int s) {
    Queue<Integer> q = new Queue<>();
    marked[s] = true;
    q.enqueue(s);

    while (!q.isEmpty()) {
      int v = q.dequeue(); // 出队
      // System.err.println("DEBUG>>> dequeue " + v + ", Queue=" + q);

      for (int w : G.adj(v)) {
        if (!marked[w]) {
          marked[w] = true;
          q.enqueue(w); // 入队
        }
      }
    }
  }

}
