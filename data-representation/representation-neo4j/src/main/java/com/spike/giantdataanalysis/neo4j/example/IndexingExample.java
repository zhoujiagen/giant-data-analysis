package com.spike.giantdataanalysis.neo4j.example;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNIndex;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;

/**
 * 索引操作示例
 */
@SuppressWarnings("deprecation")
public class IndexingExample {
  private static final Logger LOG = LoggerFactory.getLogger(IndexingExample.class);

  public static void main(String[] args) {
    GraphDatabaseService db = Neo4js.defaultDatabase();

    try (Transaction tx = db.beginTx();) {
      // (1) 显式创建索引
      explicit_indexing(db);
      query_with_explicit_indexing(db);
      update_with_explicit_indexing(db);
      query_with_explicit_indexing(db);

      // (2-1) 模式索引(使用标签Label)
      // WARNING: 模式操作与数据更新操作不能在同一个事务中
      label_indexing(db);

      tx.success();
    }

    // (2-2) 模式索引(使用标签Label)
    try (Transaction tx2 = db.beginTx();) {
      // addtional_data_for_label_indexing(db); // 添加额外数据
      query_with_label_indexing(db);

      tx2.success();
    }

    db.shutdown();// 关闭数据库

    // (3) 自动索引, WARNING: 该特性已废弃
    // auto_indexing();
  }

  @Deprecated
  @ExecuteInTransaction
  public static void auto_indexing() {
    GraphDatabaseService db = Neo4js.defaultDataBaseWithAutoIndexing();

    try (Transaction tx3 = db.beginTx();) {
      // 创建临时节点
      Node user = db.createNode(SNLabels.Users);
      user.setProperty(SNPropertyKeys.name.name(), "Cartman");
      user.setProperty(SNPropertyKeys.age.name(), 30);

      AutoIndexer<Node> autoIndexer = db.index().getNodeAutoIndexer();
      ReadableIndex<Node> index = autoIndexer.getAutoIndex();
      // 开启自动索引前创建的节点: 结果为空
      IndexHits<Node> nodes = index.get(SNPropertyKeys.name.name(), "John Johnson");
      for (Node node : nodes) {
        LOG.info("John Johnson: \n" + Neo4js.asShortString(node));
      }
      // 开启自动索引后创建的节点: 结果为新创建的节点
      nodes = index.get(SNPropertyKeys.name.name(), "Cartman");
      for (Node node : nodes) {
        LOG.info("Cartman: \n" + Neo4js.asShortString(node));
      }

      tx3.success();
    }

    db.shutdown();
  }

  /**
   * 创建模式索引
   * @param db
   * @see GraphDatabaseService#createNode(org.neo4j.graphdb.Label...) 节点可以属于多个标签, 每个标签一个索引
   */
  @ShouldExecuteInTransaction
  public static void label_indexing(GraphDatabaseService db) {
    // 当前只支持单个属性
    IndexDefinition idxDef = db.schema()//
        .indexFor(SNLabels.Movies)//
        .on(SNPropertyKeys.name.name())//
        .create();
    LOG.info(Neo4js.asShortString(idxDef));
  }

  // 额外的数据
  @ShouldExecuteInTransaction
  public static void addtional_data_for_label_indexing(GraphDatabaseService db) {
    Node movie = db.createNode(SNLabels.Movies);
    movie.setProperty(SNPropertyKeys.name.name(), "Michael Collins");

    Node user = db.createNode(SNLabels.Users);
    user.setProperty(SNPropertyKeys.name.name(), "Michael Collins");
  }

  // 使用模式索引执行查询
  public static void query_with_label_indexing(GraphDatabaseService db) {
    ResourceIterator<Node> nodes =
        db.findNodes(SNLabels.Movies, SNPropertyKeys.name.name(), "Michael Collins");

    while (nodes.hasNext()) {
      LOG.info("\n" + Neo4js.asShortString(nodes.next()));
    }
  }

  /**
   * 显式创建索引, 标识节点或关系的属性
   * @param db
   * @see IndexManager#forNodes(String)
   * @see IndexManager#forRelationships(String, java.util.Map) Lucene配置
   * @see IndexManager#forRelationships(String)
   */
  @ShouldExecuteInTransaction
  public static void explicit_indexing(GraphDatabaseService db) {
    String userName = "John Smith";
    String userEmail = "jsmith@example.org";

    Node user = db.createNode(SNLabels.Users);
    user.setProperty(SNPropertyKeys.name.name(), userName);
    user.setProperty(SNPropertyKeys.email.name(), userEmail);

    // 获取索引管理器
    IndexManager im = db.index();
    Index<Node> userIndex = im.forNodes(SNIndex.users.name());
    userIndex.add(user, SNPropertyKeys.email.name(), userEmail);
  }

  /**
   * 使用显式创建的索引执行查询
   * @param db
   */
  @ShouldExecuteInTransaction
  public static void query_with_explicit_indexing(GraphDatabaseService db) {
    String userEmail = "jsmith@example.org";

    IndexManager im = db.index();
    Index<Node> userIndex = im.forNodes(SNIndex.users.name());
    IndexHits<Node> foundResult = userIndex.get(SNPropertyKeys.email.name(), userEmail);
    for (Node user : foundResult) {
      LOG.info("\n" + Neo4js.asShortString(user));
    }
  }

  /**
   * 更新显式创建的索引: 先删除再添加
   * @param db
   * @see #explicit_indexing(GraphDatabaseService)
   */
  @ShouldExecuteInTransaction
  public static void update_with_explicit_indexing(GraphDatabaseService db) {
    String userEmail = "jsmith@example.org";
    String userNewEmail = "jsmith@new.example.org";

    IndexManager im = db.index();
    Index<Node> userIndex = im.forNodes(SNIndex.users.name());
    IndexHits<Node> foundResult = userIndex.get(SNPropertyKeys.email.name(), userEmail);
    Node user = foundResult.getSingle();

    userIndex.remove(user, SNPropertyKeys.email.name(), user);// 删除
    user.setProperty(SNPropertyKeys.email.name(), userNewEmail);
    userIndex.add(user, SNPropertyKeys.email.name(), userNewEmail);// 添加
  }

}
