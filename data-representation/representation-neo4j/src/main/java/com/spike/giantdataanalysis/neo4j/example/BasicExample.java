package com.spike.giantdataanalysis.neo4j.example;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNRelationshipType;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 嵌入式模式的基本API操作
 */
public class BasicExample {
  private static final Logger LOG = LoggerFactory.getLogger(BasicExample.class);

  public static void main(String[] args) {
    // 0 初始化 嵌入数据库
    GraphDatabaseService db = Neo4js.defaultDatabase();

    // initData(db);// 仅执行一次

    try (Transaction tx = db.beginTx();) {
      // 遍历
      // traverse_startNode(db);
      // traverse_directRelationship(db);
      traverse_relationshipWithDepth2(db);

      tx.success();
    }

    db.shutdown();// 关闭数据库
  }

  // 遍历: 寻找起始节点
  public static void traverse_startNode(GraphDatabaseService db) {
    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");
    LOG.info("\n" + Neo4js.asString(john));
  }

  // 遍历: 直接关系(John看过的电影)
  public static void traverse_directRelationship(GraphDatabaseService db) {
    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");

    // 方法1: 获取节点上所有关系, 再筛选
    // for (Relationship rel : john.getRelationships()) {
    // if (SNRelationshipType.HAS_SEEN.name().equals(rel.getType().name())) {
    // LOG.info("\n" + Neo4js.asString(rel.getEndNode()));// 关系指向的节点
    // }
    // }

    // 方法2: 直接筛选节点上所有关系
    for (Relationship rel : john.getRelationships(SNRelationshipType.HAS_SEEN)) {
      LOG.info("\n" + Neo4js.asString(rel.getEndNode()));// 关系指向的节点
    }
  }

  // 遍历: 深度为2的关系(John的朋友看过的电影, 但John没看过的电影)
  // 注意: 可以使用时间换空间的方法(直接在迭代中处理, 不使用临时存储, 即这里的Set)
  @ShouldExecuteInTransaction
  public static void traverse_relationshipWithDepth2(GraphDatabaseService db) {
    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");

    // 先遍历IS_FRIEND_OF关系
    Set<Node> friends = Sets.newHashSet();
    for (Relationship friendRel : john.getRelationships(SNRelationshipType.IS_FRIEND_OF)) {
      friends.add(friendRel.getOtherNode(john));
    }
    Set<Node> johnHasSeenMovies = Sets.newHashSet();// John看过的电影
    for (Relationship rel : john.getRelationships(SNRelationshipType.HAS_SEEN)) {
      johnHasSeenMovies.add(rel.getEndNode());
    }

    // 再遍历HAS_SEEN关系
    Set<Node> friendsHasSeenMovies = Sets.newHashSet();// John的朋友看过的电影
    for (Node friend : friends) {
      for (Relationship hasSeenRel : friend.getRelationships(Direction.OUTGOING,
        SNRelationshipType.HAS_SEEN)) {
        friendsHasSeenMovies.add(hasSeenRel.getEndNode());
      }
    }

    // 移除John看过的电影
    friendsHasSeenMovies.removeAll(johnHasSeenMovies);

    for (Node movie : friendsHasSeenMovies) {
      LOG.info(Neo4js.asString(movie));
    }
  }

  /**
   * 初始化数据
   * @param db
   */
  @ExecuteInTransaction
  public static void initData(GraphDatabaseService db) {
    try (Transaction tx = db.beginTx();) { // 开启事务
      // 1 创建节点, 设置属性
      Node john = db.createNode();
      john.addLabel(SNLabels.Users);// 标签
      john.setProperty(SNPropertyKeys.name.name(), "John Johnson");
      john.setProperty(SNPropertyKeys.year_of_brith.name(), 1982);
      Node kate = db.createNode();
      kate.addLabel(SNLabels.Users);
      kate.setProperty(SNPropertyKeys.name.name(), "Kate Smith");
      kate.setProperty(SNPropertyKeys.locked.name(), true);
      Node jack = db.createNode();
      jack.addLabel(SNLabels.Users);
      jack.setProperty(SNPropertyKeys.name.name(), "Jack Jeffries");
      jack.setProperty(SNPropertyKeys.cars_owned.name(), new String[] { "BMW", "Audi" });

      Node fargo = db.createNode();
      fargo.setProperty(SNPropertyKeys.name.name(), "Fargo");
      fargo.addLabel(SNLabels.Movies);
      Node alien = db.createNode();
      alien.setProperty(SNPropertyKeys.name.name(), "Alien");
      alien.addLabel(SNLabels.Movies);
      Node heat = db.createNode();
      heat.setProperty(SNPropertyKeys.name.name(), "Heat");
      heat.addLabel(SNLabels.Movies);

      // 2 创建关系
      john.createRelationshipTo(kate, SNRelationshipType.IS_FRIEND_OF);
      john.createRelationshipTo(jack, SNRelationshipType.IS_FRIEND_OF);

      john.createRelationshipTo(fargo, SNRelationshipType.HAS_SEEN)//
          .setProperty(SNPropertyKeys.stars.name(), 5);
      jack.createRelationshipTo(fargo, SNRelationshipType.HAS_SEEN)//
          .setProperty(SNPropertyKeys.stars.name(), 4);
      jack.createRelationshipTo(alien, SNRelationshipType.HAS_SEEN)//
          .setProperty(SNPropertyKeys.stars.name(), 5);
      kate.createRelationshipTo(heat, SNRelationshipType.HAS_SEEN)//
          .setProperty(SNPropertyKeys.stars.name(), 3);

      tx.success();// 提交事务
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
