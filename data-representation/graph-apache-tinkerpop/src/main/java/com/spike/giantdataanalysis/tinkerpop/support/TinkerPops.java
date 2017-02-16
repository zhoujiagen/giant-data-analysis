package com.spike.giantdataanalysis.tinkerpop.support;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import com.google.common.base.Preconditions;

/**
 * @author zhoujiagen
 * @see T
 * @see P
 */
public class TinkerPops {

  /**
   * 创建默认的内存中{@link TinkerGraph}; 其是AutoCloseable的.
   * @return
   */
  public static TinkerGraph newMemoryGraph() {
    TinkerGraph graph = TinkerGraph.open();
    return graph;
  }

  /**
   * @return 示例Modern图
   * @see T#id
   * @see T#label
   * @see TinkerFactory#createModern()
   * @see TinkerGraph#addVertex(Object...)
   * @see Vertex#remove()
   * @see Vertex#addEdge(String, Vertex, Object...)
   */
  public static TinkerGraph sampleMordenGraph() {
    TinkerGraph graph = TinkerGraph.open();

    // id为节点/边的标识, label代表一类节点/边
    final Vertex marko = graph.addVertex(T.id, 1, T.label, "person", "name", "marko", "age", 29);

    // 断言工具
    Preconditions.checkState(IteratorUtils.count(graph.vertices()) == 1);
    Preconditions.checkState(IteratorUtils.count(graph.edges()) == 0);

    final Vertex vadas = graph.addVertex(T.id, 2, T.label, "person", "name", "vadas", "age", 27);
    final Vertex lop = graph.addVertex(T.id, 3, T.label, "software", "name", "lop", "lang", "java");
    final Vertex josh = graph.addVertex(T.id, 4, T.label, "person", "name", "josh", "age", 32);
    final Vertex ripple =
        graph.addVertex(T.id, 5, T.label, "software", "name", "ripple", "lang", "java");
    final Vertex peter = graph.addVertex(T.id, 6, T.label, "person", "name", "peter", "age", 35);

    marko.addEdge("knows", vadas, T.id, 7, "weight", 0.5d); // label: knows
    marko.addEdge("knows", josh, T.id, 8, "weight", 1.0d);
    marko.addEdge("created", lop, T.id, 9, "weight", 0.4d);// label: created
    josh.addEdge("created", ripple, T.id, 10, "weight", 1.0d);
    josh.addEdge("created", lop, T.id, 11, "weight", 0.4d);
    peter.addEdge("created", lop, T.id, 12, "weight", 0.2d);

    return graph;
  }

}
