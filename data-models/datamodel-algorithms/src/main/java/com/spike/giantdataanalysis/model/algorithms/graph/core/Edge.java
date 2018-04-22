package com.spike.giantdataanalysis.model.algorithms.graph.core;

import com.google.common.base.Preconditions;

/**
 * 带权重的边.
 * @author zhoujiagen
 */
public class Edge implements Comparable<Edge> {

  private final int v;
  private final int w;
  private final double weight;

  public Edge(int v, int w, double weight) {
    this.v = v;
    this.w = w;
    this.weight = weight;
  }

  /** 边的权重. */
  public double weight() {
    return weight;
  }

  /** 边的任意一个顶点. */
  public int either() {
    return v;
  }

  /** 边的另一个顶点. */
  public int other(int v) {
    Preconditions.checkArgument(v == this.v || v == this.w);

    if (this.v == v) {
      return w;
    } else {
      return this.v;
    }
  }

  @Override
  public int compareTo(Edge other) {
    if (this.weight() < other.weight()) {
      return -1;
    } else if (this.weight() > other.weight()) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return v + "-" + w + " " + weight;
  }

}
