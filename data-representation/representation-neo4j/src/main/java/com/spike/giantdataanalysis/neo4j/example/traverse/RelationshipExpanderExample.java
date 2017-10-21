package com.spike.giantdataanalysis.neo4j.example.traverse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.impl.OrderedByTypeExpander;
import org.neo4j.graphdb.impl.StandardExpander;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNRelationshipType;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 关系扩展: 关系的类型/方向/跟踪顺序.
 */
public class RelationshipExpanderExample {
  private static final Logger LOG = LoggerFactory.getLogger(RelationshipExpanderExample.class);

  public static void main(String[] args) {

    // 0 初始化 嵌入数据库
    String dbPath = "target/rel_expander/";
    GraphDatabaseService db = Neo4js.database(dbPath);

    // 仅执行一次
    // initData(db);
    // Neo4js.asGraphviz(db, "MoreSocialNetwork");

    try (Transaction tx = db.beginTx();) {
      // standard_expander(db);
      // ordered_by_type_expander(db);

      depth_aware_expander(db);
      tx.success();
    }

    db.shutdown();// 关闭数据库

  }

  // 自定义路径扩展器: 不需要movieEvaluator, 在每个深度使用特定的关系
  @ShouldExecuteInTransaction
  public static void depth_aware_expander(GraphDatabaseService db) {

    // 深度感知的自定义路径扩展器
    Map<Integer, List<RelationshipType>> depthRelTypeMap =
        new HashMap<Integer, List<RelationshipType>>();
    depthRelTypeMap.put(0,
      Arrays.asList(SNRelationshipType.IS_FRIEND_OF, SNRelationshipType.WORKS_WITH));
    depthRelTypeMap.put(1, Arrays.asList(SNRelationshipType.LIKES));
    DepthAwarePathExpander expander = new DepthAwarePathExpander(depthRelTypeMap);

    TraversalDescription td = db.traversalDescription()//
        .expand(expander)//
        .evaluator(Evaluators.atDepth(2));

    Node John = findJohn(db);
    ResourceIterable<Node> nodes = td.traverse(John).nodes();
    StringBuilder sb = new StringBuilder();
    for (Node node : nodes) {
      sb.append(Neo4js.asShortString(node) + " ");
    }
    LOG.info("\n" + sb.toString());

  }

  // 标准扩展器: 查找John的朋友和同事喜欢的所有电影
  @ShouldExecuteInTransaction
  public static void standard_expander(GraphDatabaseService db) {

    TraversalDescription td = db.traversalDescription()//
        .expand(StandardExpander.DEFAULT// 标准扩展器
            .add(SNRelationshipType.WORKS_WITH)//
            .add(SNRelationshipType.IS_FRIEND_OF)//
            .add(SNRelationshipType.LIKES))//
        .evaluator(Evaluators.atDepth(2))//
        .evaluator(movieEvaluator);

    Node John = findJohn(db);
    ResourceIterable<Node> nodes = td.traverse(John).nodes();
    StringBuilder sb = new StringBuilder();
    for (Node node : nodes) {
      sb.append(Neo4js.asShortString(node) + " ");
    }
    LOG.info("\n" + sb.toString());
  }

  private static Node findJohn(GraphDatabaseService db) {
    return db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John");
  }

  // 评估器: 仅包含末尾节点是电影节点的路径
  private static Evaluator movieEvaluator = new Evaluator() {
    @Override
    public Evaluation evaluate(Path path) {
      if (path.endNode().hasProperty(SNPropertyKeys.title.name())) {
        return Evaluation.INCLUDE_AND_CONTINUE;
      }
      return Evaluation.EXCLUDE_AND_CONTINUE;
    }
  };

  // 按关系类型排序扩展器: 与标准扩展器用例类似, 但优先返回John的朋友喜欢的电影
  @ShouldExecuteInTransaction
  public static void ordered_by_type_expander(GraphDatabaseService db) {

    TraversalDescription td = db.traversalDescription()//
        .expand(OrderedByTypeExpander.DEFAULT// 按关系类型排序扩展器
            .add(SNRelationshipType.IS_FRIEND_OF)// 优先
            .add(SNRelationshipType.WORKS_WITH)//
            .add(SNRelationshipType.LIKES))//
        .evaluator(Evaluators.atDepth(2))//
        .evaluator(movieEvaluator);

    Node John = findJohn(db);
    ResourceIterable<Node> nodes = td.traverse(John).nodes();
    StringBuilder sb = new StringBuilder();
    for (Node node : nodes) {
      sb.append(Neo4js.asShortString(node) + " ");
    }
    LOG.info("\n" + sb.toString());

  }

  // 准备数据: 朋友/同事/电影的社交网络
  @ExecuteInTransaction
  public static void initData(GraphDatabaseService db) {
    try (Transaction tx = db.beginTx();) {

      Node John = db.createNode(SNLabels.Users);
      John.setProperty(SNPropertyKeys.name.name(), "John");
      Node Kate = db.createNode(SNLabels.Users);
      Kate.setProperty(SNPropertyKeys.name.name(), "Kate");
      Node Emma = db.createNode(SNLabels.Users);
      Emma.setProperty(SNPropertyKeys.name.name(), "Emma");
      Node Alex = db.createNode(SNLabels.Users);
      Alex.setProperty(SNPropertyKeys.name.name(), "Alex");
      Node Jack = db.createNode(SNLabels.Users);
      Jack.setProperty(SNPropertyKeys.name.name(), "Jack");

      Node Fargo = db.createNode(SNLabels.Movies);
      Fargo.setProperty(SNPropertyKeys.title.name(), "Fargo");
      Node TopGun = db.createNode(SNLabels.Movies);
      TopGun.setProperty(SNPropertyKeys.title.name(), "Top Gun");
      Node Alien = db.createNode(SNLabels.Movies);
      Alien.setProperty(SNPropertyKeys.title.name(), "Alien");
      Node Godfather = db.createNode(SNLabels.Movies);
      Godfather.setProperty(SNPropertyKeys.title.name(), "Godfather");
      Node GreatDictator = db.createNode(SNLabels.Movies);
      GreatDictator.setProperty(SNPropertyKeys.title.name(), "Great Dictator");

      // 注意关系创建的顺序: 从右下角开始往左上角.
      Jack.createRelationshipTo(GreatDictator, SNRelationshipType.LIKES);
      Jack.createRelationshipTo(Godfather, SNRelationshipType.LIKES);
      Alex.createRelationshipTo(Godfather, SNRelationshipType.LIKES);
      Emma.createRelationshipTo(Alien, SNRelationshipType.LIKES);
      Emma.createRelationshipTo(Jack, SNRelationshipType.WORKS_WITH);
      Kate.createRelationshipTo(Jack, SNRelationshipType.WORKS_WITH);
      Kate.createRelationshipTo(Alex, SNRelationshipType.WORKS_WITH);
      John.createRelationshipTo(TopGun, SNRelationshipType.LIKES);
      John.createRelationshipTo(Emma, SNRelationshipType.WORKS_WITH);
      John.createRelationshipTo(Kate, SNRelationshipType.IS_FRIEND_OF);
      Kate.createRelationshipTo(Fargo, SNRelationshipType.LIKES);

      tx.success();
    }

  }

  private static class DepthAwarePathExpander implements PathExpander<Object> {

    // holder to implement `reverse`
    private StandardExpander _expander = StandardExpander.DEFAULT;

    private final Map<Integer, List<RelationshipType>> depthRelTypeMap;

    public DepthAwarePathExpander(Map<Integer, List<RelationshipType>> depthRelTypeMap) {
      this.depthRelTypeMap = depthRelTypeMap;
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState<Object> state) {
      _expander.expand(path, state);

      List<RelationshipType> relTypeList = depthRelTypeMap.get(path.length());
      return path.endNode().getRelationships(relTypeList.toArray(new RelationshipType[0]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public PathExpander<Object> reverse() {
      return _expander.reverse();

    }
  }

}
