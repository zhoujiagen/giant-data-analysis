package com.spike.giantdataanalysis.tinkerpop.example;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import com.spike.giantdataanalysis.tinkerpop.support.TinkerPops;

/**
 * <pre>
 * REF: docs/recipes/index.html
 * 
 * 进展: Between Vertices
 * </pre>
 * @author zhoujiagen
 */
public class TinkerPopRecipes {

  public static void main(String[] args) {
    try (TinkerGraph graph = TinkerPops.sampleMordenGraph();) {
      GraphTraversalSource gts = graph.traversal();

      System.out.println(gts.V().toList());// 所有节点
      System.out.println(gts.E().toList()); // 所有边
      System.out.println();

      traversal(gts);
      implementation();
    }
  }

  /**
   * 遍历相关
   * @see GraphTraversal
   * @see GraphTraversal#explain()
   * @see org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__
   */
  static void traversal(GraphTraversalSource gts) {
    traversal_between_vertices(gts);

    traversal_between_vertices_sampleGraph();
  }

  static void traversal_between_vertices(GraphTraversalSource gts) {
    // (1, v)/(v, 1)的所有边
    System.out.println(gts.V(1).bothE().toList());
    // 边(1, 2)
    // 有节点获取的边的另一个节点: inV()
    System.out.println(gts.V(1).bothE().inV().has(T.id, 2).toList());// 节点
    // 由节点获取的边的另一个节点: otherV()
    System.out.println(gts.V(1).bothE().where(otherV().has(T.id, 2)).toList());// 边

    Vertex v1 = gts.V(1).next();
    Vertex v2 = gts.V(2).next();
    System.out.println(gts.V(v1).bothE().where(otherV().is(v2)).toList());
    System.out.println(gts.V(v1).outE().where(inV().is(v2)).toList());
    // 边(1, 2), (1, 3)
    System.out.println(gts.V(v1).outE().where(inV().has(T.id, P.within(2, 3))).toList());
    // (1, v), (6, v)的v
    System.out.println(gts.V(1).out().where(in().hasId(6)).toList());

    // 路径
    // ???
    System.out.println(gts.V(2, 4, 6).as("a")//
        .repeat(bothE().otherV().simplePath()).times(5).emit(hasId(P.within(2, 4, 6))).as("b")//
        .filter(select("a", "b").by(T.id).where("a", P.lt("b")))//
        .path().by().by(T.label).toList());
    System.out.println(gts.V(2, 4, 6).as("a")//
        .repeat(bothE().otherV().simplePath()).times(5).emit().toList());
  }

  /**
   * <pre>
   * [person] -completes-> [application] -appliesTo-> [job] <-created- [company]
   * 
   * coalesce ???
   * TODO READ docs/reference/index.html firstly.
   * </pre>
   * @return
   */
  @SuppressWarnings("unchecked")
  static void traversal_between_vertices_sampleGraph() {

    try (TinkerGraph graph = TinkerPops.newMemoryGraph();) {

      Vertex vBob = graph.addVertex(T.label, "person", "name", "bob");
      Vertex vStephen = graph.addVertex(T.label, "person", "name", "stephen");
      Vertex vBlueprintsInc = graph.addVertex(T.label, "company", "name", "Blueprints, Inc");
      Vertex vRexsterLlc = graph.addVertex(T.label, "company", "name", "Rexster, LLC");
      Vertex vBlueprintsJob1 = graph.addVertex(T.label, "job", "name", "job1");
      Vertex vBlueprintsJob2 = graph.addVertex(T.label, "job", "name", "job2");
      Vertex vBlueprintsJob3 = graph.addVertex(T.label, "job", "name", "job3");
      Vertex vRexsterJob1 = graph.addVertex(T.label, "job", "name", "job4");
      Vertex vAppBob1 = graph.addVertex(T.label, "application", "name", "application1");
      Vertex vAppBob2 = graph.addVertex(T.label, "application", "name", "application2");
      Vertex vAppStephen1 = graph.addVertex(T.label, "application", "name", "application3");
      Vertex vAppStephen2 = graph.addVertex(T.label, "application", "name", "application4");

      vBob.addEdge("completes", vAppBob1);
      vBob.addEdge("completes", vAppBob2);
      vStephen.addEdge("completes", vAppStephen1);
      vStephen.addEdge("completes", vAppStephen2);
      vAppBob1.addEdge("appliesTo", vBlueprintsJob1);
      vAppBob2.addEdge("appliesTo", vBlueprintsJob2);
      vAppStephen1.addEdge("appliesTo", vRexsterJob1);
      vAppStephen2.addEdge("appliesTo", vBlueprintsJob3);
      vBlueprintsInc.addEdge("created", vBlueprintsJob1, "creationDate", "12/20/2015");
      vBlueprintsInc.addEdge("created", vBlueprintsJob2, "creationDate", "12/15/2015");
      vBlueprintsInc.addEdge("created", vBlueprintsJob3, "creationDate", "12/16/2015");
      vRexsterLlc.addEdge("created", vRexsterJob1, "creationDate", "12/18/2015");

      // 查询
      // [person] -completes-> [application] -appliesTo-> [job] <-created- [company]
      GraphTraversalSource gts = graph.traversal();
      Vertex falseVertex = graph.addVertex("name", false);

      List<Map<String, Object>> queryResult = //
          gts.V(vRexsterJob1).as("job")
              //
              .inE("created").as("created")
              //
              .outV().as("company")
              //
              .<Vertex> select("job").<Vertex> coalesce(//
                in("appliesTo").where(in("completes").is(vStephen)), constant(falseVertex))
              .as("application")//
              .select("job", "company", "created", "application") //
              .by("name").by("name").by("creationDate").by("name").toList();
      System.out.println(queryResult);

      System.out.println();

      queryResult =
          gts.V(vRexsterJob1, vBlueprintsJob1).as("job")
              //
              .inE("created").as("created")
              //
              .outV().as("company")
              //
              .select("job").coalesce(//
                in("appliesTo").where(in("completes").is(vBob)), constant(falseVertex))
              .as("application")//
              .select("job", "company", "created", "application")//
              .by("name").by("name").by("creationDate").by("name").toList();

      System.out.println(queryResult);
      falseVertex.remove(); // 移除常量节点
    }
  }

  /**
   * 实现相关
   */
  static void implementation() {

  }
}
