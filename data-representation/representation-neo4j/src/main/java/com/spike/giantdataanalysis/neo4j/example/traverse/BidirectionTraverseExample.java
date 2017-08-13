package com.spike.giantdataanalysis.neo4j.example.traverse;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.SideSelectorPolicies;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNRelationshipType;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 双向遍历示例: 开始侧/结束侧/碰撞评估/侧选择器.
 */
public class BidirectionTraverseExample {
  private static final Logger LOG = LoggerFactory.getLogger(BidirectionTraverseExample.class);

  public static void main(String[] args) {

    // 0 初始化 嵌入数据库
    String dbPath = "target/simple_social_network/";
    GraphDatabaseService db = Neo4js.database(dbPath);

    try (Transaction tx = db.beginTx();) {
      bidirection_traverse(db);

      tx.success();
    }

    db.shutdown();// 关闭数据库
  }

  /**
   * 双向遍历
   * 
   * <pre>
   * ==================================================
   * Path[length=4]
   * --------------------------------------------------
   * Users 5{(name=Emma)}
   * =KNOWS{}=>
   * Users 2{(name=Kate)}
   * =KNOWS{}=>
   * Users 0{(name=Jane)}
   * =KNOWS{}=>
   * Users 1{(name=John)}
   * =KNOWS{}=>
   * Users 3{(name=Jack)}
   * ==================================================
   * 
   * ==================================================
   * Path[length=3]
   * --------------------------------------------------
   * Users 5{(name=Emma)}
   * =KNOWS{}=>
   * Users 2{(name=Kate)}
   * =KNOWS{}=>
   * Users 1{(name=John)}
   * =KNOWS{}=>
   * Users 3{(name=Jack)}
   * ==================================================
   * 
   * </pre>
   * @param db
   */
  @ShouldExecuteInTransaction
  public static void bidirection_traverse(GraphDatabaseService db) {
    BidirectionalTraversalDescription btd =
        db.bidirectionalTraversalDescription()
            // 开始侧
            .startSide(
              db.traversalDescription().relationships(SNRelationshipType.KNOWS, Direction.OUTGOING)
                  .uniqueness(Uniqueness.NODE_PATH))//
            // 结束侧
            .endSide(
              db.traversalDescription().relationships(SNRelationshipType.KNOWS, Direction.INCOMING)
                  .uniqueness(Uniqueness.NODE_PATH))//
            // 碰撞评估器
            .collisionEvaluator(new Evaluator() {
              @Override
              public Evaluation evaluate(Path path) {
                return Evaluation.INCLUDE_AND_CONTINUE;
              }
            })
            // 侧选择器
            .sideSelector(SideSelectorPolicies.ALTERNATING, 100);// 交替选择, 并设置最大深度

    Node Emma = findUser(db, "Emma");
    Node Jack = findUser(db, "Jack");

    ResourceIterable<Path> paths = btd.traverse(Emma, Jack);
    StringBuilder sb = new StringBuilder();
    for (Path path : paths) {
      sb.append(Neo4js.asString(path)).append("\n");
    }
    LOG.info("\n" + sb.toString());
  }

  private static Node findUser(GraphDatabaseService db, String name) {
    return db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), name);
  }

}
