package com.spike.giantdataanalysis.tinkerpop.example;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TinkerPop Gremlin实现的示例
 * @author zhoujiagen
 * @see T
 * @see T#label
 * @see T#id
 */
public final class TinkerGraphGremlinExample {
  private static final Logger LOG = LoggerFactory.getLogger(TinkerGraphGremlinExample.class);

  public static final String VERTEX_KEY_NAME = "name";
  public static final String VERTEX_KEY_AGE = "age";
  public static final String VERTEX_KEY_LANG = "lang";

  public static final String EDGE_LABEL_CREATED = "created";
  public static final String EDGE_KEY_WEIGHT = "weight";

  public static void main(String[] args) {
    // 图
    try (TinkerGraph graph = TinkerGraph.open();) {
      // 节点/结点
      Vertex marko = graph.addVertex(//
        VERTEX_KEY_NAME, "marko",//
        VERTEX_KEY_AGE, 29);
      Vertex lop = graph.addVertex(//
        VERTEX_KEY_NAME, "lop",//
        VERTEX_KEY_LANG, "java");

      // 边
      marko.addEdge(//
        EDGE_LABEL_CREATED, // label
        lop, // inVertex
        EDGE_KEY_WEIGHT, 0.6d);

      // 创建节点上的索引
      graph.createIndex(VERTEX_KEY_NAME, Vertex.class);

      // 遍历
      GraphTraversalSource gts = graph.traversal();
      GraphTraversal<Vertex, Object> gt =
          gts.V().has(VERTEX_KEY_NAME, "marko").out().values(VERTEX_KEY_LANG);
      // 提取结果
      List<Object> result = gt.toList();
      LOG.info(result.toString());
    }

  }
}
