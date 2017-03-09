package com.spike.giantdataanalysis.tinkerpop.example;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import com.spike.giantdataanalysis.tinkerpop.support.TinkerPops;

/**
 * <pre>
 * REF: docs/tutorials/getting-started/index.html
 * 
 * gremlin shell中默认导入的类:
 * org.apache.tinkerpop.gremlin.structure.T
 * org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__
 * </pre>
 * @author zhoujiagen
 */
public class TinkerPopTutorial {

  public static void main(String[] args) {
    try (TinkerGraph graph = TinkerPops.sampleMordenGraph();) {
      simpleTraverse(graph);

      complexTraverse(graph);
    }
  }

  /**
   * 复杂的遍历, 记录遍历查询回答的问题
   * @param graph
   * @see P#gt(Object)
   * @see P#within(Object...)
   */
  static void complexTraverse(TinkerGraph graph) {
    GraphTraversalSource g = graph.traversal();

    // 查询marko创建的软件名称
    System.out.println(//
        g.V().has("name", "marko").out("created").values("name").toList());

    // 查询vadas, marko的年龄
    System.out.println(//
        g.V().has("name", P.within("vadas", "marko")).values("age").toList());
    // 查询vadas, marko的平均年龄
    System.out.println(//
        g.V().has("name", P.within("vadas", "marko")).values("age").mean().toList());

    // 查询marko创建的软件的协作者姓名
    // 中间结果别名label: as()
    System.out.println(//
        g.V().has("name", "marko").as("marko").out("created").in("created").where(P.neq("marko"))
            .values("name").toList());

    // 投影: select()
    // 查找长度为2的边
    System.out.println(//
        g.V().as("a").out().as("b").out().as("c").select("a", "b", "c").toList());

    // 分组: group()
    // 按label分组
    System.out.println(//
        g.V().group().by(T.label).toList());
    // 提取组中元素的属性
    System.out.println(//
        g.V().group().by(T.label).by("name").toList());
  }

  /**
   * 简单的遍历
   * @param graph
   */
  static void simpleTraverse(TinkerGraph graph) {
    GraphTraversalSource g = graph.traversal();

    // 所有节点
    GraphTraversal<Vertex, Vertex> vgt = g.V();
    List<Vertex> allVertexts = vgt.toList();
    System.out.println(allVertexts);

    // 指定节点
    System.out.println(g.V(1).next());
    // 节点属性
    System.out.println(g.V(1).values("name").next());
    // 节点的出边: v1 -knows-> v
    System.out.println(g.V(1).outE("knows").toList());
    // 节点的出边关联节点上的属性 v1 -knows-> v(name=?)
    System.out.println(g.V(1).outE("knows").inV().values("name").toList());
    System.out.println(g.V(1).out("knows").values("name").toList());// 简写形式: out()
    // 节点的属性值过滤, P.gt(30) 谓词实现
    System.out.println(g.V(1).out("knows").has("age", P.gt(30)).values("name").toList());
  }

}
