package com.spike.giantdataanalysis.titan.example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.titan.support.TitanAppException;
import com.spike.giantdataanalysis.titan.support.TitanAppGraphDataOperation;
import com.spike.giantdataanalysis.titan.support.TitanAppGraphSchemaOperation;
import com.spike.giantdataanalysis.titan.support.TitanAppNonTxGraphSchemaOperation;
import com.spike.giantdataanalysis.titan.support.TitanOps;
import com.spike.giantdataanalysis.titan.support.TitanSchemaOps;
import com.spike.giantdataanalysis.titan.support.TitanStrings;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.schema.SchemaAction;
import com.thinkaurelius.titan.core.schema.TitanGraphIndex;
import com.thinkaurelius.titan.core.schema.TitanManagement;

/**
 * 索引操作示例.
 * <p>
 * 注意: 不要在事务中创建索引.
 */
public class ExampleIndexing {

  // not a good method here.
  static TitanGraphIndex byNameCompositeIndex;
  static TitanGraphIndex byNameAndAgeCompositeIndex;

  public static void main(String[] args) {
    try {
      // 测试使用
      TitanGraph graph = TitanOps.newMemoryGraph();
      // 连接HBase和ES, 见hbase-es-setup.txt
      // TitanGraph graph = TitanOps.newGraph("src/main/resources/conf/titan-hbase-es.properties");

      // 符合索引
      // graph_index_composite_index(graph);
      // graph_index_composite_index_data(graph);
      // graph_index_composite_index_query(graph);

      // 混合索引: 需要索引后端
      // graph_index_mixed_index(graph);

      // 节点视角的索引
      vertex_centric_index(graph);

      TitanOps.close(graph);// 关闭
      TitanOps.clean(graph);// 清理测试数据
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // global retrieval operation
  // fast and efficient
  // limit to equality lookup for a particular predefined combination of property keys
  static void graph_index_composite_index(final TitanGraph graph) {
    // 不在事务中创建索引
    graph.tx().rollback();

    new TitanAppNonTxGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        // 创建复合索引
        PropertyKey namePK =
            TitanSchemaOps.makePropertyKey(tm, "name").dataType(String.class).make();
        PropertyKey agePK = TitanSchemaOps.makePropertyKey(tm, "age").make();
        byNameCompositeIndex =
            tm.buildIndex("byNameComposite", Vertex.class).addKey(namePK).buildCompositeIndex();
        byNameAndAgeCompositeIndex =
            tm.buildIndex("byNameAndAgeComposite", Vertex.class).addKey(namePK).addKey(agePK)
                .buildCompositeIndex();

        // 创建唯一索引
        tm.buildIndex("byNameUnique", Vertex.class).addKey(namePK).unique().buildCompositeIndex();

        tm.commit();
      }
    }.execute(graph);

    // 等待索引操作完成
    new TitanAppNonTxGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        System.out.println(tm.containsGraphIndex("byNameComposite"));
        System.out.println(tm.containsGraphIndex("byNameAndAgeComposite"));
      }
    }.execute(graph);

    // 示例: 重新索引已有数据
    new TitanAppGraphSchemaOperation() {

      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        try {
          tm.updateIndex(byNameCompositeIndex, SchemaAction.REINDEX).get();
          tm.updateIndex(byNameAndAgeCompositeIndex, SchemaAction.REINDEX).get();
          tm.commit();
        } catch (InterruptedException | ExecutionException e) {
          throw TitanAppException.newException(e);
        }
      }
    };

  }

  static void graph_index_composite_index_data(final TitanGraph graph) {
    new TitanAppGraphDataOperation() {
      @Override
      public void operation(TitanTransaction tx) throws TitanAppException {
        Map<String, Object> properties = Maps.newHashMap();
        properties.put("name", "hercules");
        properties.put("age", 30);
        TitanOps.createV(graph, null, properties);
      }
    }.execute(graph);
  }

  static void graph_index_composite_index_query(final TitanGraph graph) {
    GraphTraversalSource gts = TitanOps.traversal(graph);

    Vertex hercules = gts.V().has("name", "hercules").next();
    System.out.println(TitanStrings.repr(hercules));

    hercules = gts.V().has("age", 30).has("name", "hercules").next();
    System.out.println(TitanStrings.repr(hercules));

    // // 不支持: 索引中键必须都在查询中出现
    hercules = gts.V().has("age", 30).next();
    System.out.println(TitanStrings.repr(hercules));

    // 不支持: 只支持相等查询
    // REF: org.apache.tinkerpop.gremlin.process.traversal.P<V>
    hercules = gts.V().has("name", "hercules").has("age", P.inside(20, 50)).next();
    System.out.println(TitanStrings.repr(hercules));
  }

  // global retrieval operation
  // support multiple condition predicates on any combination of indexed keys
  // WARNING: need index backend
  static void graph_index_mixed_index(final TitanGraph graph) {
    // 不在事务中创建索引
    graph.tx().rollback();

    // 后端索引的名称
    final String backingIndex = "search";

    // 创建索引
    TitanManagement tm = TitanSchemaOps.openManagement(graph);
    PropertyKey namePK = tm.makePropertyKey("nameMixed").dataType(String.class).make();
    PropertyKey agePK = tm.makePropertyKey("ageMixed").dataType(Integer.class).make();
    @SuppressWarnings("unused")
    TitanGraphIndex nameIndex =
        tm.buildIndex("nameMixed", Vertex.class).addKey(namePK).buildMixedIndex(backingIndex);
    @SuppressWarnings("unused")
    TitanGraphIndex ageIndex =
        tm.buildIndex("ageMixed", Vertex.class).addKey(agePK).buildMixedIndex(backingIndex);
    tm.commit();

    // // 重新索引既有数据
    // tm = TitanOps.openManagement(graph);
    // tm.updateIndex(nameIndex, SchemaAction.REINDEX);
    // tm.updateIndex(ageIndex, SchemaAction.REINDEX);
    // tm.commit();

    // 准备数据
    Transaction tx = TitanOps.tx(graph);
    Map<String, Object> properties = Maps.newHashMap();
    properties.put("nameMixed", "hercules");
    properties.put("ageMixed", 30);
    TitanOps.createV(graph, null, properties);
    tx.commit();

    // 执行查询
    // may wait for a few seconds, since the NETWORK
    GraphTraversalSource gts = TitanOps.traversal(graph);
    Vertex hercules = gts.V().has("nameMixed", "hercules").has("ageMixed", P.inside(20, 50)).next();
    System.out.println(TitanStrings.repr(hercules));
  }

  // speed up traversals through vertices with many incident edges
  static void vertex_centric_index(final TitanGraph graph) {
    // 不在事务中创建索引
    graph.tx().rollback();

    // 创建索引
    TitanManagement tm = TitanSchemaOps.openManagement(graph);
    PropertyKey timePK = tm.makePropertyKey("time").dataType(Long.class).make();
    PropertyKey ratingPK = tm.makePropertyKey("rating").dataType(Double.class).make();
    EdgeLabel battled = tm.makeEdgeLabel("battled").make();
    tm.buildEdgeIndex(battled, "battlesByRatingAndTime", Direction.OUT, Order.decr, ratingPK,
      timePK);
    tm.commit();

    // 准备数据
    Transaction tx = TitanOps.tx(graph);
    Map<String, Object> properties = Maps.newHashMap();
    properties.put("name", "hercules");
    properties.put("age", 30);
    Vertex _hercules = TitanOps.createV(graph, null, properties);
    System.out.println(TitanStrings.repr(_hercules));

    for (int i = 1; i < 10; i++) {
      properties.clear();
      properties.put("name", "monster" + i);
      Vertex _monster = TitanOps.createV(graph, null, properties);
      System.out.println(TitanStrings.repr(_monster));
      properties.clear();
      properties.put("time", i * 10);
      properties.put("rating", ((double) i));
      Edge edge = TitanOps.createE(graph, "battled", _hercules, _monster, properties);
      System.out.println(TitanStrings.repr(edge));
    }
    tx.commit();

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // 执行查询
    GraphTraversalSource gts = TitanOps.traversal(graph);
    Vertex hercules = gts.V().has("name", "hercules").next();
    System.out.println(TitanStrings.repr(hercules));
    List<Vertex> vertexs = gts.V(hercules).outE("battled").has("rating", P.gt(3.0)).inV().toList();
    repr(vertexs);
    vertexs =
        gts.V(hercules).outE("battled").has("rating", 5.0).has("time", P.inside(10, 60)).inV()
            .toList();
    repr(vertexs);
    // 不支持索引查询
    vertexs = gts.V(hercules).outE("battled").has("time", P.inside(10, 60)).inV().toList();
    repr(vertexs);
  }

  static void repr(List<Vertex> vertexs) {
    System.out.println();
    if (CollectionUtils.isEmpty(vertexs)) return;

    for (Vertex vertex : vertexs) {
      System.out.println(TitanStrings.repr(vertex));
    }

  }

}
