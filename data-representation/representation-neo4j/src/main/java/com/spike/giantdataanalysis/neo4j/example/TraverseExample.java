package com.spike.giantdataanalysis.neo4j.example;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNRelationshipType;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 遍历API基本操作
 */
public class TraverseExample {
  private static final Logger LOG = LoggerFactory.getLogger(TraverseExample.class);

  public static void main(String[] args) {
    GraphDatabaseService db = Neo4js.defaultDatabase();

    try (Transaction tx = db.beginTx();) {
      // 遍历
      traverse_relationshipWithDepth2(db);
      // traverse_relationshipWithDepth2_with_Evaluator(db);

      tx.success();
    }

    db.shutdown();// 关闭数据库
  }

  // 遍历API: 深度为2的关系(John的朋友看过的电影)
  @ShouldExecuteInTransaction
  public static void traverse_relationshipWithDepth2(GraphDatabaseService db) {
    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");

    // 遍历描述, 注意关系的顺序
    TraversalDescription td = db.traversalDescription()//
        .relationships(SNRelationshipType.IS_FRIEND_OF)//
        .relationships(SNRelationshipType.HAS_SEEN, Direction.OUTGOING)//
        // .uniqueness(Uniqueness.NODE_GLOBAL)// 唯一性: 全局节点
        .evaluator(Evaluators.atDepth(2)); // 深度

    Traverser t = td.traverse(john); // 设置遍历的开始节点

    for (Path path : t) {
      LOG.info("\n" + Neo4js.asString(path));
    }
  }

  // 遍历: 深度为2的关系(John的朋友看过的电影, 但John没看过的电影)
  @ShouldExecuteInTransaction
  public static void traverse_relationshipWithDepth2_with_Evaluator(GraphDatabaseService db) {
    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");

    // 遍历描述, 注意关系和评估器的顺序
    TraversalDescription td = db.traversalDescription()//
        .relationships(SNRelationshipType.IS_FRIEND_OF)//
        .relationships(SNRelationshipType.HAS_SEEN, Direction.OUTGOING)//
        .uniqueness(Uniqueness.NODE_GLOBAL)// 唯一性: 全局节点
        .evaluator(Evaluators.atDepth(2)) // 深度
        .evaluator(new MyNodeFiletringEvaluator(john));// 使用自定义评估器

    Traverser t = td.traverse(john); // 设置遍历的开始节点
    ResourceIterable<Node> movies = t.nodes();
    for (Node movie : movies) {
      LOG.info(Neo4js.asString(movie));
    }
  }

  /**
   * 自定义评估器: 排除指定的用户已看过的电影.
   * @see
   */
  public static class MyNodeFiletringEvaluator implements Evaluator {
    // 指定的用户节点
    private final Node userNode;

    public MyNodeFiletringEvaluator(Node userNode) {
      this.userNode = userNode;
    }

    @Override
    public Evaluation evaluate(Path path) {
      // 遍历中当前节点
      Node currentNode = path.endNode();

      if (!currentNode.hasLabel(SNLabels.Movies)) {
        return Evaluation.EXCLUDE_AND_CONTINUE;
      }

      for (Relationship rel : currentNode.getRelationships(Direction.INCOMING,
        SNRelationshipType.HAS_SEEN)) {
        if (rel.getStartNode().equals(userNode)) {
          return Evaluation.EXCLUDE_AND_CONTINUE;
        }
      }

      return Evaluation.INCLUDE_AND_CONTINUE;
    }

  }

}
