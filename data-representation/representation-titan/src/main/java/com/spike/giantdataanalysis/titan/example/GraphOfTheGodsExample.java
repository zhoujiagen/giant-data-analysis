package com.spike.giantdataanalysis.titan.example;

import java.io.File;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.Multiplicity;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.thinkaurelius.titan.core.schema.ConsistencyModifier;
import com.thinkaurelius.titan.core.schema.TitanGraphIndex;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.thinkaurelius.titan.example.GraphOfTheGodsFactory;

/**
 * REF: http://s3.thinkaurelius.com/docs/titan/current/server.html
 * @author zhoujiagen
 * @see GraphOfTheGodsFactory
 */
public class GraphOfTheGodsExample {
  private static final Logger LOG = LoggerFactory.getLogger(GraphOfTheGodsExample.class);

  public static void main(String[] args) {
    System.out.println(new File(".").getAbsolutePath());
    TitanGraph graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");

    // load only once
    load(graph);

    GraphTraversalSource gts = graph.traversal();
    List<Object> names = gts.V().values("name").toList();

    LOG.info(names.toString());

    graph.close();
    // TitanCleanup.clear(graph); // 清空图中索引和数据
  }

  public static final String INDEX_NAME = "search";

  /**
   * <pre>
   * 创建TitanGraph
   * 
   * storage backend: berkeleyje
   * index backend: elasticsearch
   * </pre>
   * @param directory
   * @return
   */
  public static TitanGraph create(final String directory) {
    TitanFactory.Builder config = TitanFactory.build();
    config.set("storage.backend", "berkeleyje");
    config.set("storage.directory", directory);
    config.set("index." + INDEX_NAME + ".backend", "elasticsearch");
    config.set("index." + INDEX_NAME + ".directory", directory + File.separator + "es");
    config.set("index." + INDEX_NAME + ".elasticsearch.local-mode", true);
    config.set("index." + INDEX_NAME + ".elasticsearch.client-only", false);

    TitanGraph graph = config.open();
    GraphOfTheGodsFactory.load(graph);
    return graph;
  }

  public static void load(final TitanGraph graph) {
    load(graph, INDEX_NAME, true);
  }

  public static void load(final TitanGraph graph, String mixedIndexName,
      boolean uniqueNameCompositeIndex) {

    // 1 创建Schema
    loadSchema(graph, mixedIndexName, uniqueNameCompositeIndex);

    // 2 创建节点和边
    loadData(graph, mixedIndexName, uniqueNameCompositeIndex);
  }

  public static void loadSchema(final TitanGraph graph, String mixedIndexName,
      boolean uniqueNameCompositeIndex) {

    // Create Schema
    TitanManagement mgmt = graph.openManagement();

    // 节点属性
    final PropertyKey name = mgmt.makePropertyKey("name").dataType(String.class).make();
    // name索引: name
    TitanManagement.IndexBuilder nameIndexBuilder =
        mgmt.buildIndex("name", Vertex.class).addKey(name);
    if (uniqueNameCompositeIndex) nameIndexBuilder.unique(); // 唯一性索引
    TitanGraphIndex namei = nameIndexBuilder.buildCompositeIndex(); // 聚合索引
    mgmt.setConsistency(namei, ConsistencyModifier.LOCK);
    final PropertyKey age = mgmt.makePropertyKey("age").dataType(Integer.class).make();
    if (null != mixedIndexName) {
      // vertices索引: age
      mgmt.buildIndex("vertices", Vertex.class).addKey(age).buildMixedIndex(mixedIndexName);// 混合索引
    }
    // 边属性
    final PropertyKey time = mgmt.makePropertyKey("time").dataType(Integer.class).make();
    final PropertyKey reason = mgmt.makePropertyKey("reason").dataType(String.class).make();
    final PropertyKey place = mgmt.makePropertyKey("place").dataType(Geoshape.class).make();
    if (null != mixedIndexName) {
      // edges索引: reason, place
      mgmt.buildIndex("edges", Edge.class).addKey(reason).addKey(place)
          .buildMixedIndex(mixedIndexName);
    }

    // 边标签
    mgmt.makeEdgeLabel("father").multiplicity(Multiplicity.MANY2ONE).make();
    mgmt.makeEdgeLabel("mother").multiplicity(Multiplicity.MANY2ONE).make();
    EdgeLabel battled = mgmt.makeEdgeLabel("battled").signature(time).make();
    // battlesByTime索引: time
    mgmt.buildEdgeIndex(battled, "battlesByTime", Direction.BOTH, Order.decr, time);
    mgmt.makeEdgeLabel("lives").signature(reason).make();
    mgmt.makeEdgeLabel("pet").make();
    mgmt.makeEdgeLabel("brother").make();

    // 节点标签
    mgmt.makeVertexLabel("titan").make();
    mgmt.makeVertexLabel("location").make();
    mgmt.makeVertexLabel("god").make();
    mgmt.makeVertexLabel("demigod").make();
    mgmt.makeVertexLabel("human").make();
    mgmt.makeVertexLabel("monster").make();

    mgmt.commit();
  }

  public static void loadData(final TitanGraph graph, String mixedIndexName,
      boolean uniqueNameCompositeIndex) {

    TitanTransaction tx = graph.newTransaction();
    // vertices

    Vertex saturn = tx.addVertex(T.label, "titan", "name", "saturn", "age", 10000);
    Vertex sky = tx.addVertex(T.label, "location", "name", "sky");
    Vertex sea = tx.addVertex(T.label, "location", "name", "sea");
    Vertex jupiter = tx.addVertex(T.label, "god", "name", "jupiter", "age", 5000);
    Vertex neptune = tx.addVertex(T.label, "god", "name", "neptune", "age", 4500);
    Vertex hercules = tx.addVertex(T.label, "demigod", "name", "hercules", "age", 30);
    Vertex alcmene = tx.addVertex(T.label, "human", "name", "alcmene", "age", 45);
    Vertex pluto = tx.addVertex(T.label, "god", "name", "pluto", "age", 4000);
    Vertex nemean = tx.addVertex(T.label, "monster", "name", "nemean");
    Vertex hydra = tx.addVertex(T.label, "monster", "name", "hydra");
    Vertex cerberus = tx.addVertex(T.label, "monster", "name", "cerberus");
    Vertex tartarus = tx.addVertex(T.label, "location", "name", "tartarus");

    // edges
    jupiter.addEdge("father", saturn);
    jupiter.addEdge("lives", sky, "reason", "loves fresh breezes");
    jupiter.addEdge("brother", neptune);
    jupiter.addEdge("brother", pluto);

    neptune.addEdge("lives", sea).property("reason", "loves waves");
    neptune.addEdge("brother", jupiter);
    neptune.addEdge("brother", pluto);

    hercules.addEdge("father", jupiter);
    hercules.addEdge("mother", alcmene);
    hercules.addEdge("battled", nemean, "time", 1, "place", Geoshape.point(38.1f, 23.7f));
    hercules.addEdge("battled", hydra, "time", 2, "place", Geoshape.point(37.7f, 23.9f));
    hercules.addEdge("battled", cerberus, "time", 12, "place", Geoshape.point(39f, 22f));

    pluto.addEdge("brother", jupiter);
    pluto.addEdge("brother", neptune);
    pluto.addEdge("lives", tartarus, "reason", "no fear of death");
    pluto.addEdge("pet", cerberus);

    cerberus.addEdge("lives", tartarus);

    // commit the transaction to disk
    tx.commit();
  }
}
