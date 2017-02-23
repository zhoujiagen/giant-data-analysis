package com.spike.giantdataanalysis.tinkerpop.support;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import com.google.common.base.Preconditions;

// @formatter:off
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

  /**
   * @return 示例Crew图
   * @see TinkerFactory#createTheCrew()
   */
  public static TinkerGraph sampleCrewGraph() {
    final Configuration conf = new BaseConfiguration();
    // 节点属性的基数为列表, 即节点具有多个同名属性
    conf.setProperty(TinkerGraph.GREMLIN_TINKERGRAPH_DEFAULT_VERTEX_PROPERTY_CARDINALITY, VertexProperty.Cardinality.list.name());
    final TinkerGraph g = TinkerGraph.open(conf);
    
    // 节点
    final Vertex marko = g.addVertex(T.id, 1, T.label, "person", "name", "marko");
    final Vertex stephen = g.addVertex(T.id, 7, T.label, "person", "name", "stephen");
    final Vertex matthias = g.addVertex(T.id, 8, T.label, "person", "name", "matthias");
    final Vertex daniel = g.addVertex(T.id, 9, T.label, "person", "name", "daniel");
    final Vertex gremlin = g.addVertex(T.id, 10, T.label, "software", "name", "gremlin");
    final Vertex tinkergraph = g.addVertex(T.id, 11, T.label, "software", "name", "tinkergraph");

    // 节点属性
    marko.property(VertexProperty.Cardinality.list, "location", "san diego", "startTime", 1997, "endTime", 2001);
    marko.property(VertexProperty.Cardinality.list, "location", "santa cruz", "startTime", 2001, "endTime", 2004);
    marko.property(VertexProperty.Cardinality.list, "location", "brussels", "startTime", 2004, "endTime", 2005);
    marko.property(VertexProperty.Cardinality.list, "location", "santa fe", "startTime", 2005);

    stephen.property(VertexProperty.Cardinality.list, "location", "centreville", "startTime", 1990, "endTime", 2000);
    stephen.property(VertexProperty.Cardinality.list, "location", "dulles", "startTime", 2000, "endTime", 2006);
    stephen.property(VertexProperty.Cardinality.list, "location", "purcellville", "startTime", 2006);

    matthias.property(VertexProperty.Cardinality.list, "location", "bremen", "startTime", 2004, "endTime", 2007);
    matthias.property(VertexProperty.Cardinality.list, "location", "baltimore", "startTime", 2007, "endTime", 2011);
    matthias.property(VertexProperty.Cardinality.list, "location", "oakland", "startTime", 2011, "endTime", 2014);
    matthias.property(VertexProperty.Cardinality.list, "location", "seattle", "startTime", 2014);

    daniel.property(VertexProperty.Cardinality.list, "location", "spremberg", "startTime", 1982, "endTime", 2005);
    daniel.property(VertexProperty.Cardinality.list, "location", "kaiserslautern", "startTime", 2005, "endTime", 2009);
    daniel.property(VertexProperty.Cardinality.list, "location", "aachen", "startTime", 2009);

    // 边
    marko.addEdge("develops", gremlin, T.id, 13, "since", 2009);
    marko.addEdge("develops", tinkergraph, T.id, 14, "since", 2010);
    marko.addEdge("uses", gremlin, T.id, 15, "skill", 4);
    marko.addEdge("uses", tinkergraph, T.id, 16, "skill", 5);

    stephen.addEdge("develops", gremlin, T.id, 17, "since", 2010);
    stephen.addEdge("develops", tinkergraph, T.id, 18, "since", 2011);
    stephen.addEdge("uses", gremlin, T.id, 19, "skill", 5);
    stephen.addEdge("uses", tinkergraph, T.id, 20, "skill", 4);

    matthias.addEdge("develops", gremlin, T.id, 21, "since", 2012);
    matthias.addEdge("uses", gremlin, T.id, 22, "skill", 3);
    matthias.addEdge("uses", tinkergraph, T.id, 23, "skill", 3);

    daniel.addEdge("uses", gremlin, T.id, 24, "skill", 5);
    daniel.addEdge("uses", tinkergraph, T.id, 25, "skill", 3);

    gremlin.addEdge("traverses", tinkergraph, T.id, 26);

    // 图的元数据信息
    g.variables().set("creator", "marko");
    g.variables().set("lastModified", 2014);
    g.variables().set("comment", "this graph was created to provide examples and test coverage for tinkerpop3 api advances");
    
    return g;
  }
  
  public static void result(GraphTraversal<?, ?> gt){
    if(gt == null) return;
    
    // see DefaultTraversal.toString()
    System.out.println("STEPS: " + gt.toString());
    System.out.println("RESULT: ");
    while(gt.hasNext()) {
      System.out.println(gt.next());
    }
    System.out.println();
  }
  
  public static void path(GraphTraversal<?, ?> gt){
    if(gt == null) return;
    
    System.out.println("PATH: " + gt.path());
    System.out.println("RESULT: ");
    while(gt.hasNext()) {
      System.out.println(gt.next());
    }
    System.out.println();
  }
  
  public static void result(Object object) {
    if(object instanceof Iterable) {
      Iterable<?> itable = (Iterable<?>)object;
      Iterator<?> it = itable.iterator();
      while(it.hasNext()){
        System.out.print(it.next() + " ");
      }
      System.out.println();
    }
    
    System.out.println(object);
  }
  
  public static void introspect(Path path) {
    // object -> Set[label]
    StringBuilder sb = new StringBuilder();

    sb.append("SIZE: " + path.size() + "\n");
    sb.append("OBJECTS/LABELS:\n");
    List<Object> objects  = path.objects();
    List<Set<String>> labels  = path.labels();
    for(int i = 0, len = objects.size(); i < len;i++) {
      sb.append("\t" + objects.get(i) + ": " + labels.get(i)+"\n");
    }
    
    System.out.println(sb.toString());
  }
  
  
  public static void explain(GraphTraversal<?, ?> gt) {
    System.out.println(gt.explain());
  }
  
}
