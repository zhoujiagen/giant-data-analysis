package com.spike.giantdataanalysis.neo4j.example.traverse;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.impl.StandardExpander;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNRelationshipType;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 遍历中管理唯一性示例
 * @see Uniqueness
 * @see GraphAlgoFactory
 */
public class TraverseWithUniquenessExample {
  private static final Logger LOG = LoggerFactory.getLogger(TraverseWithUniquenessExample.class);

  public static void main(String[] args) {

    // 0 初始化 嵌入数据库
    String dbPath = "target/simple_social_network/";
    GraphDatabaseService db = Neo4js.database(dbPath);

    // 仅执行一次
    initData(db);
    Neo4js.asGraphviz(db, "SimpleSocialNetwork");

    try (Transaction tx = db.beginTx();) {
      /**
       * <pre>
       * NODE_GLOBAL: 遍历中每个节点只能访问一次
       * 
       * ==================================================
       * Path[length=1]
       * --------------------------------------------------
       * Users 0{(name=Jane)}
       * =KNOWS{}=>
       * Users 1{(name=John)}
       * ==================================================
       * 
       * <pre>
       */
      uniqueness(db, Uniqueness.NODE_GLOBAL);

      /**
       * <pre>
       * NODE_PATH: 节点可以在结果路径集中出现多次, 但关系在结果路径集中只能出现一次
       * 
       * ==================================================
       * Path[length=1]
       * --------------------------------------------------
       * Users 0{(name=Jane)}
       * =KNOWS{}=>
       * Users 1{(name=John)}
       * ==================================================
       * 
       * ==================================================
       * Path[length=2]
       * --------------------------------------------------
       * Users 0{(name=Jane)}
       * =KNOWS{}=>
       * Users 2{(name=Kate)}
       * =KNOWS{}=>
       * Users 1{(name=John)}
       * ==================================================
       * 
       * </pre>
       */
      uniqueness(db, Uniqueness.NODE_PATH);

      tx.success();
    }

    db.shutdown();// 关闭数据库
  }

  // 用例: 查询出将Jane介绍给Ben的直接联系人
  @ShouldExecuteInTransaction
  public static void uniqueness(GraphDatabaseService db, Uniqueness uniqueness) {

    final Node Ben = findUser(db, "Ben");

    Evaluator evaluator = new Evaluator() {
      @Override
      public Evaluation evaluate(Path path) {
        Node currentNode = path.endNode(); // 当前节点
        if (currentNode.getId() == Ben.getId()) { // 已到Ben节点
          return Evaluation.EXCLUDE_AND_PRUNE;
        }

        // 当前节点到Ben节点的深度为1的最短路径
        Path singlePath = GraphAlgoFactory//
            .shortestPath(StandardExpander.DEFAULT.add(SNRelationshipType.KNOWS), 1)//
            .findSinglePath(currentNode, Ben);
        if (singlePath != null) {
          return Evaluation.INCLUDE_AND_CONTINUE;
        } else {
          return Evaluation.EXCLUDE_AND_CONTINUE;
        }

      }
    };

    TraversalDescription td = db.traversalDescription()//
        .relationships(SNRelationshipType.KNOWS, Direction.OUTGOING)//
        .evaluator(evaluator)//
        .uniqueness(uniqueness); // 设置遍历的唯一性约束

    Node Jane = findUser(db, "Jane");
    ResourceIterable<Path> paths = td.traverse(Jane);
    StringBuilder sb = new StringBuilder();
    for (Path path : paths) {
      sb.append(Neo4js.asString(path)).append("\n");
    }
    LOG.info("\n" + sb.toString());
  }

  private static Node findUser(GraphDatabaseService db, String name) {
    return db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), name);
  }

  // 准备数据: 简单的社交网络
  @ExecuteInTransaction
  public static void initData(GraphDatabaseService db) {
    try (Transaction tx = db.beginTx();) {

      Node Jane = db.createNode(SNLabels.Users);
      Jane.setProperty(SNPropertyKeys.name.name(), "Jane");
      Node John = db.createNode(SNLabels.Users);
      John.setProperty(SNPropertyKeys.name.name(), "John");
      Node Kate = db.createNode(SNLabels.Users);
      Kate.setProperty(SNPropertyKeys.name.name(), "Kate");
      Node Jack = db.createNode(SNLabels.Users);
      Jack.setProperty(SNPropertyKeys.name.name(), "Jack");
      Node Ben = db.createNode(SNLabels.Users);
      Ben.setProperty(SNPropertyKeys.name.name(), "Ben");
      Node Emma = db.createNode(SNLabels.Users);
      Emma.setProperty(SNPropertyKeys.name.name(), "Emma");

      // 注意关系创建的顺序: 从右下角开始往左上角.
      // 注意: KNOWS是双向关系.
      Kate.createRelationshipTo(Emma, SNRelationshipType.KNOWS);
      Emma.createRelationshipTo(Kate, SNRelationshipType.KNOWS);

      John.createRelationshipTo(Ben, SNRelationshipType.KNOWS);
      Ben.createRelationshipTo(John, SNRelationshipType.KNOWS);

      John.createRelationshipTo(Jack, SNRelationshipType.KNOWS);
      Jack.createRelationshipTo(John, SNRelationshipType.KNOWS);

      Kate.createRelationshipTo(John, SNRelationshipType.KNOWS);
      John.createRelationshipTo(Kate, SNRelationshipType.KNOWS);

      Jane.createRelationshipTo(Kate, SNRelationshipType.KNOWS);
      Kate.createRelationshipTo(Jane, SNRelationshipType.KNOWS);

      Jane.createRelationshipTo(John, SNRelationshipType.KNOWS);
      John.createRelationshipTo(Jane, SNRelationshipType.KNOWS);

      tx.success();
    }

  }
}
