package com.spike.giantdataanalysis.model.algorithms.graph.core;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.algorithms.adt.Bag;
import com.spike.giantdataanalysis.model.algorithms.support.Statistics;

/**
 * 无向图.
 * <p>
 * 顶点由从0开始的整数标识; 边采用邻接表数组表示(邻接表为便于处理沿着图的边从一个顶点移动到另一个顶点).
 * @author zhoujiagen
 */
public class Graph implements IGraph {
  private final int V; // 顶点数量
  private int E; // 边数量
  private Bag<Integer>[] adj; // 邻接表数组

  /** 构造顶点数为V的无向图. */
  @SuppressWarnings("unchecked")
  public Graph(int V) {
    this.V = V;
    this.E = 0;
    adj = (Bag<Integer>[]) new Bag[V];
    for (int v = 0; v < V; v++) {
      adj[v] = new Bag<Integer>();
    }
  }

  /** 顶点数量. */
  @Override
  public int V() {
    return this.V;
  }

  /** 边数量. */
  @Override
  public int E() {
    return this.E;
  }

  /** 添加由顶点v和w构成的边. */
  @Override
  public void addEdge(int v, int w) {
    // 两个顶点的邻接表中均需添加
    adj[v].add(w);
    adj[w].add(v);
    E++;
  }

  /** 获取顶点v的邻接顶点. */
  @Override
  public Iterable<Integer> adj(int v) {
    return adj[v];
  }

  /** 输出图. */
  public void dumpGraphviz() {
    StringBuilder sb = new StringBuilder();
    sb.append("graph G {\n");
    sb.append("\t").append("size=100; ratio=0.8; splines= line").append("\n");
    sb.append("\t").append("node [shape=circle, fixedsize=true, width=0.3]").append("\n");
    for (int v = 0; v < V; v++) {
      Bag<Integer> bag = adj[v];
      for (Integer w : bag) {
        if (v <= w) {
          // 将图打散
          if (Statistics.bit()) {
            sb.append("\t").append(v + " -- " + w).append("\n");
          } else {
            sb.append("\t").append(w + " -- " + v).append("\n");
          }
        }
      }
    }
    sb.append("}");

    System.out.println(sb.toString());
  }

  /** 输出邻接表数组. */
  public void dumpAdjGraphviz() {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph GAdj {\n");
    sb.append("\t").append("rankdir = LR; splines=polyline;").append("\n");
    sb.append("\t").append("edge [arrowsize=0.5, minlen=1];").append("\n");
    sb.append("\t").append("node [shape=record, width=.1, height=.1];").append("\n");

    List<String> l = Lists.newArrayList();
    for (int v = 0; v < V; v++) {
      l.add(" <p" + v + "> " + v);
    }
    sb.append("\t").append("adj [label = \"" + Joiner.on("|").join(l) + "\", height = 1]")
        .append("\n\n");

    sb.append("\t").append("node[ height=0.1 ];").append("\n");
    for (int v = 0; v < V; v++) {
      Bag<Integer> bag = adj[v];
      if (bag.isEmpty()) {
        continue;
      }
      Iterator<Integer> it = bag.iterator();
      int currentAdjV = it.next();
      sb.append("\t").append("adj" + v + "" + currentAdjV + " [label=" + currentAdjV + "]")
          .append("\n");
      sb.append("\t").append("adj:p" + v + " -> adj" + v + "" + currentAdjV).append("\n");
      while (it.hasNext()) {
        int w = it.next();
        sb.append("\t").append("adj" + v + "" + w + " [label=" + w + "]").append("\n");
        sb.append("\t").append("adj" + v + "" + currentAdjV + " -> " + "adj" + v + "" + w)
            .append("\n");
        currentAdjV = w;
      }
    }
    sb.append("\t").append("").append("\n");
    sb.append("}");

    System.out.println(sb.toString());
  }

}
