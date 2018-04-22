package com.spike.giantdataanalysis.model.algorithms.graph;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.algorithms.graph.core.Graph;
import com.spike.giantdataanalysis.model.algorithms.search.IST;
import com.spike.giantdataanalysis.model.algorithms.search.SequentialSearchST;

/**
 * 符号图: 顶点名称为字符串.
 * <p>
 * 使用符号表, 记录顶点名称与顶点编号之间的对应关系.
 * @author zhoujiagen
 */
public class SymbolGraph {

  private IST<String, Integer> st; // 顶点名称 -> 顶点编号
  private String[] keys; // 顶点的名称, 索引: 顶点, 值: 顶点名称
  private Graph G; // 内部的图

  public SymbolGraph(String stream, String sep) {
    st = new SequentialSearchST<>();
    String[] inputKeys = stream.split(sep);
    for (String inputKey : inputKeys) {
      if (!st.contains(inputKey)) {
        st.put(inputKey, st.size());
      }
    }
    keys = new String[st.size()];
    for (String name : st.keys()) {
      keys[st.get(name)] = name;
    }
    G = new Graph(st.size());
  }

  public void addEdge(String source, String target) {
    Preconditions.checkState(this.contains(source), "Vertex source not exist!");
    Preconditions.checkState(this.contains(target), "Vertex target not exist!");

    G.addEdge(st.get(source), st.get(target));
  }

  /** 是否包含顶点名称. */
  public boolean contains(String s) {
    return st.contains(s);
  }

  /** 顶点名称对应的顶点编号. */
  public int index(String s) {
    return st.get(s);
  }

  /** 顶点编号对应的顶点名称. */
  public String name(int v) {
    return keys[v];
  }

  /** 内部的图. */
  public Graph G() {
    return G;
  }
}
