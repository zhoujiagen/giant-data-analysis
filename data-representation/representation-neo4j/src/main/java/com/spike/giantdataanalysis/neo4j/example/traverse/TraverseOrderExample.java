package com.spike.giantdataanalysis.neo4j.example.traverse;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 遍历顺序: 深度优先, 广度优先.
 * <p>
 * 一般来说, 每个节点的关系越多, 广度优先需要的内存越多.
 */
public class TraverseOrderExample {
  private static final Logger LOG = LoggerFactory.getLogger(TraverseOrderExample.class);

  public static void main(String[] args) {

    // 0 初始化 嵌入数据库
    String dbPath = "target/tree/";
    GraphDatabaseService db = Neo4js.database(dbPath);

    // 仅执行一次
    initData(db);
    Neo4js.asGraphviz(db, "SampleTree");

    try (Transaction tx = db.beginTx();) {
      depth_first(db);
      breadth_first(db);

      tx.success();
    }

    db.shutdown();// 关闭数据库

  }

  // 深度优先
  // 先访问当前节点的之前没有访问过的第一个子节点
  // 如果所有的后代子节点均访问过, 返回没有访问过的子节点中的第一个子节点
  // 1 2 5 6 3 7 8 4 9
  @ShouldExecuteInTransaction
  public static void depth_first(GraphDatabaseService db) {
    TraversalDescription td = db.traversalDescription()//
        .relationships(TreeGraphRelationshipType.CHILD, Direction.OUTGOING)//
        .depthFirst();// ~

    Node _1 = db.getNodeById(0l);
    ResourceIterable<Node> nodes = td.traverse(_1).nodes();

    StringBuilder sb = new StringBuilder();
    for (Node node : nodes) {
      sb.append(node.getProperty(TreeGraphPropertyKeys.id.name()) + " ");
    }
    LOG.info("\n" + sb.toString());
  }

  // 广度优先: 访问子节点之前, 首先访问当前节点的所有同级节点
  // 1 2 3 4 5 6 7 8 9
  @ShouldExecuteInTransaction
  public static void breadth_first(GraphDatabaseService db) {
    TraversalDescription td = db.traversalDescription()//
        .relationships(TreeGraphRelationshipType.CHILD, Direction.OUTGOING)//
        .breadthFirst(); // !

    Node _1 = db.getNodeById(0l);
    ResourceIterable<Node> nodes = td.traverse(_1).nodes();

    StringBuilder sb = new StringBuilder();
    for (Node node : nodes) {
      sb.append(node.getProperty(TreeGraphPropertyKeys.id.name()) + " ");
    }
    LOG.info("\n" + sb.toString());
  }

  // 准备数据: 9个节点的树状图
  @ExecuteInTransaction
  public static void initData(GraphDatabaseService db) {
    try (Transaction tx = db.beginTx();) {

      Node _1 = db.createNode();
      _1.setProperty(TreeGraphPropertyKeys.id.name(), "1");
      Node _2 = db.createNode();
      _2.setProperty(TreeGraphPropertyKeys.id.name(), "2");
      Node _3 = db.createNode();
      _3.setProperty(TreeGraphPropertyKeys.id.name(), "3");
      Node _4 = db.createNode();
      _4.setProperty(TreeGraphPropertyKeys.id.name(), "4");
      Node _5 = db.createNode();
      _5.setProperty(TreeGraphPropertyKeys.id.name(), "5");
      Node _6 = db.createNode();
      _6.setProperty(TreeGraphPropertyKeys.id.name(), "6");
      Node _7 = db.createNode();
      _7.setProperty(TreeGraphPropertyKeys.id.name(), "7");
      Node _8 = db.createNode();
      _8.setProperty(TreeGraphPropertyKeys.id.name(), "8");
      Node _9 = db.createNode();
      _9.setProperty(TreeGraphPropertyKeys.id.name(), "9");

      // 注意关系创建的顺序: 从右下角开始往左上角.
      _4.createRelationshipTo(_9, TreeGraphRelationshipType.CHILD);
      _3.createRelationshipTo(_8, TreeGraphRelationshipType.CHILD);
      _3.createRelationshipTo(_7, TreeGraphRelationshipType.CHILD);
      _2.createRelationshipTo(_6, TreeGraphRelationshipType.CHILD);
      _2.createRelationshipTo(_5, TreeGraphRelationshipType.CHILD);
      _1.createRelationshipTo(_4, TreeGraphRelationshipType.CHILD);
      _1.createRelationshipTo(_3, TreeGraphRelationshipType.CHILD);
      _1.createRelationshipTo(_2, TreeGraphRelationshipType.CHILD);

      tx.success();
    }

  }

  // 树状图中关系定义
  public enum TreeGraphRelationshipType implements RelationshipType {
    CHILD
  }

  // 属性的键
  public enum TreeGraphPropertyKeys {
    id
  }
}
