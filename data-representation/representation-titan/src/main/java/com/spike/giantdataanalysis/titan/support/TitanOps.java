package com.spike.giantdataanalysis.titan.support;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.core.TitanException;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.util.TitanCleanup;
import com.thinkaurelius.titan.diskstorage.BackendException;
import com.thinkaurelius.titan.diskstorage.configuration.backend.CommonsConfiguration;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;

/**
 * Titan中图数据操作工具类.
 * <p>
 * 基本上是TitanGraph的封装.
 */
public abstract class TitanOps {

  private static final Logger LOG = LoggerFactory.getLogger(TitanOps.class);

  /**
   * 创建{@link TitanGraph}.
   * @param conf
   * @return
   */
  public static TitanGraph newGraph(Map<String, Object> conf) {
    Preconditions.checkArgument(MapUtils.isNotEmpty(conf),
      "Argument conf should not be null or empty!");

    Configuration graphConf = new BaseConfiguration();
    for (String key : conf.keySet()) {
      graphConf.setProperty(key, conf.get(key));
    }
    TitanGraph result = TitanFactory.open(graphConf);
    return result;
  }

  /**
   * 创建{@link TitanGraph}.
   * @param propertyFilePath 例如src/main/resources/conf/titan-cassandra-es.properties
   * @return
   */
  public static TitanGraph newGraph(String propertyFilePath) {
    Preconditions.checkArgument(StringUtils.isNotBlank(propertyFilePath),
      "Argument propertyFilePath should not be blank!");
    Preconditions.checkArgument(Files.exists(Paths.get(propertyFilePath)),
      "File path propertyFilePath should exist!");

    TitanGraph result = TitanFactory.open(propertyFilePath);
    return result;
  }

  /**
   * 创建内存存储后端的{@link TitanGraph}.
   * <p>
   * WARNING: 仅用于测试和概念验证.
   * @return
   */
  public static TitanGraph newMemoryGraph() {
    TitanGraph result =
        TitanFactory.build()
            .set(TitanConf.storage_backend_key, TitanConf.storage_backend_value_inmemory).open();
    return result;
  }

  /**
   * 创建{@link TitanGraph}上事务.
   * @param graph
   * @return
   */
  public static TitanTransaction newTransaction(final TitanGraph graph) {
    Preconditions.checkArgument(graph != null);

    return graph.newTransaction();
  }

  /**
   * 获取{@link TitanGraph}中事务.
   * @param graph
   * @return
   */
  public static Transaction tx(final TitanGraph graph) {
    Preconditions.checkArgument(graph != null);

    return graph.tx();
  }

  /**
   * 获取{@link TitanGraph}的遍历客户端.
   * @param graph
   * @return
   */
  public static GraphTraversalSource traversal(final TitanGraph graph) {
    Preconditions.checkArgument(graph != null);

    return graph.traversal();
  }

  /**
   * 创建节点.
   * <p>
   * WARNING: non transactional.
   * @param graph
   * @param label 可以为null
   * @param properties 如果为null, 设置为{@link #DEFAULT_PROPERTY_NAME}, label
   * @return
   */
  public static Vertex createV(final Graph graph, final String label,
      final Map<String, Object> properties) {
    Preconditions.checkArgument(graph != null);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Create Vertex with label[{}], properties[{}]", label, properties);
    }

    Vertex v = null;

    if (label != null) {
      v = graph.addVertex(label);
    } else {
      v = graph.addVertex();
    }

    if (properties == null) {
      v.property(TitanSchemaOps.DEFAULT_PROPERTY_NAME,
        label == null ? TitanSchemaOps.DEFAULT_LABEL_NAME : label);
    } else {
      for (String propName : properties.keySet()) {
        v.property(propName, properties.get(propName));
      }
    }

    return v;
  }

  /**
   * 创建边.
   * <p>
   * WARNING: non transactional.
   * @param graph
   * @param label 如果为null, 设置为{@link #DEFAULT_PROPERTY_NAME}
   * @param from
   * @param to
   * @param properties
   * @return
   */
  public static Edge createE(final Graph graph, final String label, final Vertex from,
      final Vertex to, final Map<String, Object> properties) {
    Preconditions.checkArgument(graph != null);
    Preconditions.checkArgument(from != null);
    Preconditions.checkArgument(to != null);

    Edge edge = from.addEdge(label, to);

    if (properties == null) {
      edge.property(TitanSchemaOps.DEFAULT_PROPERTY_NAME,
        label == null ? TitanSchemaOps.DEFAULT_LABEL_NAME : label);
    } else {
      for (String propName : properties.keySet()) {
        edge.property(propName, properties.get(propName));
      }
    }

    return edge;
  }

  /**
   * 查找节点.
   * @param graph
   * @param propName 属性名称
   * @param propValue 属性值
   * @return
   */
  public static List<Vertex> findV(final Graph graph, String propName, String propValue) {
    Preconditions.checkArgument(graph != null);
    Preconditions.checkArgument(propName != null);
    Preconditions.checkArgument(propValue != null);

    GraphTraversalSource gts = graph.traversal();
    GraphTraversal<Vertex, Vertex> gt = gts.V().has(propName, propValue);
    List<Vertex> vertexs = gt.toList();
    return vertexs;
  }

  /**
   * 查找节点.
   * @param graph
   * @param propName
   * @param propValue
   * @return
   */
  public static Vertex findSingleV(final Graph graph, String propName, String propValue) {
    List<Vertex> vertexs = findV(graph, propName, propValue);
    if (vertexs != null && vertexs.size() > 0) {
      return vertexs.get(0);
    }
    return null;
  }

  /**
   * 关闭图.
   * @param graph
   * @throws TitanAppException
   */
  public static void close(final TitanGraph graph) throws TitanAppException {
    Preconditions.checkArgument(graph != null);

    try {
      graph.close();
    } catch (TitanException e) {
      throw TitanAppException.newException(e);
    }
  }

  /**
   * 清空图中数据.
   * <p>
   * WARNING: 仅在开发和测试中使用
   * @param graph
   * @see TitanCleanup#clear(TitanGraph)
   * @see GraphTraversalSource.V().drop().iterate();
   */
  public static void clean(final Graph graph) throws TitanAppException {
    Preconditions.checkArgument(graph != null);

    if (graph instanceof TitanGraph) {
      TitanGraph tg = (TitanGraph) graph;
      Preconditions.checkArgument(!tg.isOpen(),
        "Graph needs to be shut down before it can be cleared.");
      try {
        TitanCleanup.clear(tg);
      } catch (IllegalArgumentException e) {
        throw TitanAppException.newException("TitanGraph is not shut down!", e);
      } catch (TitanException e) {
        throw TitanAppException.newException("Clean TitanGraph backend storage failed!", e);
      }

    } else {
      LOG.warn("Graph[{}] is not TitanGraph, just clean the data without schema.", graph.toString());
      GraphTraversalSource g = graph.traversal();
      g.V().drop().iterate();
      g.E().drop().iterate();
      graph.tx().commit();
    }
  }

  /**
   * 清空图中数据.
   * <p>
   * WARNING: 仅在开发和测试中使用
   * @param titanGraph
   * @throws BackendException
   */
  public static void clean(final TitanGraph titanGraph) throws TitanAppException {
    Preconditions.checkArgument(titanGraph != null);

    // convert org.apache.commons.configuration.Configuration to GraphDatabaseConfiguration
    GraphDatabaseConfiguration config = new GraphDatabaseConfiguration(//
        new CommonsConfiguration(titanGraph.configuration()));
    try {
      config.getBackend().clearStorage();
    } catch (BackendException e) {
      throw TitanAppException.newException(e);
    }
  }
}
