package com.spike.giantdataanalysis.neo4j.supports;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.traversal.Paths;
import org.neo4j.kernel.GraphDatabaseDependencies;
import org.neo4j.kernel.internal.EmbeddedGraphDatabase;
import org.neo4j.visualization.graphviz.GraphStyle;
import org.neo4j.visualization.graphviz.MyDefaultStyleConfiguration;
import org.neo4j.visualization.graphviz.MyGraphvizWriter;
import org.neo4j.visualization.graphviz.MyNodeStyle;
import org.neo4j.visualization.graphviz.MyRelationshipStyle;
import org.neo4j.walk.Walker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

public class Neo4js {

  private static final Logger LOG = LoggerFactory.getLogger(Neo4js.class);

  /**
   * 默认的内嵌数据库
   * @return
   */
  public static GraphDatabaseService defaultDatabase() {
    String dbPath = "target/data/";
    return database(dbPath);
  }

  /**
   * WARNING: 自动索引特性已废弃.
   * @return
   */
  @Deprecated
  public static GraphDatabaseService defaultDataBaseWithAutoIndexing() {
    String dbPath = "data/";
    Map<String, String> configuration = Maps.newHashMap();
    // 节点属性自动索引
    configuration.put(GraphDatabaseSettings.node_auto_indexing.name(), "true");
    // 关系属性自动索引
    configuration.put(GraphDatabaseSettings.relationship_auto_indexing.name(), "true");
    // 可自动索引的节点属性
    configuration.put(GraphDatabaseSettings.node_keys_indexable.name(), "name, dateOfBirth");
    // 可自动索引的关系属性
    configuration.put(GraphDatabaseSettings.relationship_keys_indexable.name(), "type, name");

    return database(dbPath, configuration);
  }

  public static GraphDatabaseService database(String dbPath) {
    File storeDir = new File(dbPath);
    LOG.info("创建/定位内嵌数据库: {}", dbPath);
    GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir);
    return db;
  }

  public static GraphDatabaseService database(String dbPath, Map<String, String> configuration) {
    File storeDir = new File(dbPath);
    LOG.info("创建/定位内嵌数据库: {}", dbPath);
    EmbeddedGraphDatabase db = new EmbeddedGraphDatabase(//
        storeDir, configuration, GraphDatabaseDependencies.newDependencies());
    return db;
  }

  /**
   * 按Label和属性值查找节点, 事务性操作.
   * @param db
   * @param label
   * @param propertyKey
   * @param propertyValue
   * @return
   */
  @ExecuteInTransaction
  public static ResourceIterator<Node> findNode(GraphDatabaseService db, Label label,
      String propertyKey, Object propertyValue) {
    ResourceIterator<Node> nodes = null;
    try (Transaction tx = db.beginTx();) { // 开启事务
      nodes = findNodeNonTx(db, label, propertyKey, propertyValue);
      tx.success();
    }
    return nodes;
  }

  /**
   * 按Label和属性值查找节点, 非事务性操作.
   * @param db
   * @param label
   * @param propertyKey
   * @param propertyValue
   * @return
   */
  @ShouldExecuteInTransaction
  public static ResourceIterator<Node> findNodeNonTx(GraphDatabaseService db, Label label,
      String propertyKey, Object propertyValue) {
    Preconditions.checkArgument(db != null, "Argument db should not be null!");
    Preconditions.checkArgument(label != null, "Argument label should not be null!");
    Preconditions.checkArgument(propertyKey != null, "Argument propertyKey should not be null!");
    Preconditions.checkArgument(propertyValue != null, //
      "Argument propertyValue should not be null!");

    ResourceIterator<Node> nodes = db.findNodes(label, propertyKey, propertyValue);
    return nodes;
  }

  /**
   * 生成节点的字符串表示, 便于查看.
   * @param node
   * @return
   */
  @ShouldExecuteInTransaction
  public static String asString(Node node) {
    Preconditions.checkArgument(node != null, "Argument node should not be null!");

    StringBuilder sb = new StringBuilder();
    sb.append(Strings.repeat("=", 50)).append("\n");
    sb.append("Node[id=" + node.getId()).append("]\n");// 获取ID不需要在事务中
    sb.append(Strings.repeat("-", 50)).append("\n");
    sb.append("Labels=");
    for (Label label : node.getLabels()) {
      sb.append(label.name() + " ");
    }
    sb.append("\n");
    sb.append(Strings.repeat("-", 50)).append("\n");
    for (String propKey : node.getPropertyKeys()) {
      sb.append(propKey).append("=").append(node.getProperty(propKey)).append("\n");
    }
    sb.append(Strings.repeat("=", 50)).append("\n");
    return sb.toString();
  }

  @ShouldExecuteInTransaction
  public static String asShortString(Node node) {
    Preconditions.checkArgument(node != null, "Argument node should not be null!");
    StringBuilder sb = new StringBuilder();

    for (Label label : node.getLabels()) {
      sb.append(label.name() + " ");
    }
    sb.append(node.getId());

    sb.append("{");
    Object value = null;
    for (String propKey : node.getPropertyKeys()) {
      sb.append("(");
      sb.append(propKey).append("=");

      value = node.getProperty(propKey);
      if (value instanceof String[]) {
        sb.append("[");
        sb = Joiner.on(",").appendTo(sb, (String[]) value);
        sb.append("])");
      } else {
        sb.append(value).append(")");
      }
    }
    sb.append("}");
    return sb.toString();
  }

  @ShouldExecuteInTransaction
  public static String asShortString(Relationship relationship) {
    Preconditions.checkArgument(relationship != null, "Argument relationship should not be null!");

    StringBuilder sb = new StringBuilder();

    sb.append("=");
    sb.append(relationship.getType().name());
    sb.append("{");
    Object value = null;
    for (String propKey : relationship.getAllProperties().keySet()) {
      sb.append("(");
      sb.append(propKey).append("=");

      value = relationship.getProperty(propKey);
      if (value instanceof String[]) {
        sb.append("[");
        sb = Joiner.on(",").appendTo(sb, (String[]) value);
        sb.append("])");
      } else {
        sb.append(value).append(")");
      }

    }
    sb.append("}");
    sb.append("=>");
    return sb.toString();

  }

  /**
   * 生成路径的字符串表示, 便于查看.
   * @param path
   * @return
   * @see Paths#pathToString(Path, org.neo4j.graphdb.traversal.Paths.PathDescriptor)
   * @see PathPrinter
   */
  @ShouldExecuteInTransaction
  public static String asString(Path path) {
    Preconditions.checkArgument(path != null, "Argument path should not be null!");

    StringBuilder sb = new StringBuilder();
    sb.append(Strings.repeat("=", 50)).append("\n");
    sb.append("Path[length=" + path.length()).append("]\n");// 获取ID不需要在事务中
    sb.append(Strings.repeat("-", 50)).append("\n");
    Iterable<Node> nodes = path.nodes();
    Iterator<Relationship> rels = path.relationships().iterator();
    Relationship rel = null;
    for (Node node : nodes) {
      sb.append(asShortString(node)).append("\n");
      if (rels.hasNext()) {
        rel = rels.next();
        sb.append(asShortString(rel)).append("\n");
      }
    }
    sb.append(Strings.repeat("=", 50)).append("\n");
    return sb.toString();
  }

  public static class PathPrinter implements Paths.PathDescriptor<Path> {
    private final String nodePropertyKey;

    public PathPrinter(String nodePropertyKey) {
      this.nodePropertyKey = nodePropertyKey;
    }

    @Override
    public String nodeRepresentation(Path path, Node node) {
      return "(" + node.getProperty(nodePropertyKey, "") + ")";
    }

    @Override
    public String relationshipRepresentation(Path path, Node from, Relationship relationship) {
      String prefix = "--", suffix = "--";
      if (from.equals(relationship.getEndNode())) {
        prefix = "<--";
      } else {
        suffix = "-->";
      }
      return prefix + "[" + relationship.getType().name() + "]" + suffix;
    }

  }

  /**
   * 生成模式索引定义的字符串表示, 便于查看.
   * @param idxDef
   * @return
   */
  @ShouldExecuteInTransaction
  public static String asShortString(IndexDefinition idxDef) {
    Preconditions.checkArgument(idxDef != null, "Argument idxDef should not be null!");

    StringBuilder sb = new StringBuilder();

    sb.append("Label(" + idxDef.getLabel() + "), ");
    sb.append("Property(" + idxDef.getPropertyKeys() + ")");

    return sb.toString();
  }

  /**
   * 生成Cypher查询结果的字符串表示, 便于查看.
   * @param idxDef
   * @return
   */
  @ShouldExecuteInTransaction
  public static String asString(Result result) {
    Preconditions.checkArgument(result != null, "Argument result should not be null!");

    return result.resultAsString();
  }

  /**
   * 将数据库全量转换为Graphviz图
   * @param dbPath
   * @param dotName
   */
  @ExecuteInTransaction
  public static void asGraphviz(GraphDatabaseService db, String dotName) {
    Preconditions.checkArgument(db != null, "Argument db should not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(dotName),
      "Argument dotName should not be empty!");

    try (Transaction tx = db.beginTx();) {
      // 风格配置
      MyDefaultStyleConfiguration styleConfiguration = new MyDefaultStyleConfiguration();
      // 节点风格
      MyNodeStyle nodeStyle = new MyNodeStyle(styleConfiguration);
      // 关系风格
      MyRelationshipStyle relationshipStyle = new MyRelationshipStyle(styleConfiguration);
      // 图风格
      GraphStyle graphStyle = new GraphStyle(nodeStyle, relationshipStyle);
      // graphviz输出
      MyGraphvizWriter writer = new MyGraphvizWriter(graphStyle);
      // 图遍历
      Walker walker = Walker.fullGraph(db);
      try {
        writer.emit(new File("graphviz/" + dotName + ".dot"), walker);
      } catch (IOException e) {
        LOG.info("asGraphviz failed", e);
      }

      tx.success();
    }

  }
}
