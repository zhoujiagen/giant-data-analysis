package com.spike.giantdataanalysis.neo4j.example;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ShouldExecuteInTransaction;
import com.spike.giantdataanalysis.neo4j.supports.cypher.CypherQueryBuilder;

/**
 * <pre>
 * Cypher示例.
 * 
 * 完整的语法见The Neo4j Developer Manual v3.2 5-12章.
 * </pre>
 */
public class CypherExample {
  private static final Logger LOG = LoggerFactory.getLogger(CypherExample.class);

  public static void main(String[] args) {

    GraphDatabaseService db = Neo4js.defaultDatabase();

    try (Transaction tx = db.beginTx();) {
      // simple(db);
      // node(db);
      // index(db);
      // skip_limit(db);
      // regular_expression(db);
      // create_node(db);
      update_node(db);

      tx.success();
    }

    db.shutdown();// 关闭数据库
  }

  /** 节点ID, 节点对象, 多个节点ID, 节点属性字符串字面量 */
  public static void node(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.MATCH("(n)");
    builder.WHERE("id(n) = $id"); // 节点ID
    builder.RETURN("n.name");
    String cql = builder.build().toString();
    Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("id", 0);
    executeCypherQuery(db, cql, parameters);

    Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");
    builder = new CypherQueryBuilder();
    builder.MATCH("(n)");
    builder.WHERE("n = $john"); // 节点对象
    builder.RETURN("n.name");
    cql = builder.build().toString();
    parameters = Maps.newHashMap();
    parameters.put("john", john);
    executeCypherQuery(db, cql, parameters);

    builder = new CypherQueryBuilder();
    builder.MATCH("(n)");
    builder.WHERE("id(n) IN $ids"); // 多个借点
    builder.RETURN("n.name");
    cql = builder.build().toString();
    parameters = Maps.newHashMap();
    parameters.put("ids", Lists.newArrayList(0, 1, 2));
    executeCypherQuery(db, cql, parameters);

    builder = new CypherQueryBuilder();
    builder.MATCH("(n:Users)");
    builder.WHERE("n.name = $name"); // 节点属性字符串字面量
    builder.RETURN("n");
    cql = builder.build().toString();
    parameters = Maps.newHashMap();
    parameters.put("name", "John Johnson");
    executeCypherQuery(db, cql, parameters);
  }

  /**
   * 索引值和索引查询
   * @see {@link IndexingExample#explicit_indexing(GraphDatabaseService)}
   * @see {@link IndexingExample#update_with_explicit_indexing(GraphDatabaseService)}
   */
  public static void index(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.START("n=node:users(email = $email)"); // 注意$email不要加引号
    builder.RETURN("n");
    String cql = builder.build().toString();
    Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("email", "jsmith@new.example.org");
    executeCypherQuery(db, cql, parameters);

    builder = new CypherQueryBuilder();
    builder.START("n=node:users($query)");
    builder.RETURN("n");
    cql = builder.build().toString();
    parameters = Maps.newHashMap();
    parameters.put("query", "email:jsmith@new.example.org"); // Lucene语法
    executeCypherQuery(db, cql, parameters);
  }

  /** SKIP和LIMIT */
  public static void skip_limit(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.MATCH("(n:Users)");
    builder.RETURN("n.name");
    builder.SKIP("$s");
    builder.LIMIT("$l");
    String cql = builder.build().toString();
    Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("s", 1);
    parameters.put("l", 2);
    executeCypherQuery(db, cql, parameters);
  }

  /** 正则表达式 */
  public static void regular_expression(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.MATCH("(n:Users)");
    builder.WHERE("n.name =~ $regex");
    builder.RETURN("n.name");
    String cql = builder.build().toString();
    Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("regex", ".*h.*"); // 包含h的名字
    executeCypherQuery(db, cql, parameters);
  }

  /** 创建带属性的节点 */
  public static void create_node(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.CREATE("n=($props)");
    builder.RETURN("n");
    String cql = builder.build().toString();
    Map<String, Object> props = Maps.newHashMap();
    props.put(SNPropertyKeys.name.name(), "Andres");
    props.put(SNPropertyKeys.age.name(), 30);
    Map<String, Object> parameters = Maps.newHashMap();
    parameters.put("props", props);
    executeCypherQuery(db, cql, parameters);

    // 创建多个节点
    List<Map<String, Object>> mapList = Lists.newArrayList();
    props = Maps.newHashMap();
    props.put(SNPropertyKeys.name.name(), "Bob");
    props.put(SNPropertyKeys.age.name(), 28);
    mapList.add(props);
    props = Maps.newHashMap();
    props.put(SNPropertyKeys.name.name(), "David");
    props.put(SNPropertyKeys.age.name(), 32);
    mapList.add(props);

    builder = new CypherQueryBuilder();
    builder.UNWIND("$props AS properties");
    builder.CREATE("(n:Users)");
    builder.SET("n = properties");
    builder.RETURN("n");
    cql = builder.build().toString();

    parameters = Maps.newHashMap();
    parameters.put("props", mapList);
    executeCypherQuery(db, cql, parameters);
  }

  /** 更新节点属性 */
  public static void update_node(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.MATCH("(n:Users)");
    builder.WHERE("n.name='Bob'");
    builder.SET("n = $props");
    builder.RETURN("n");
    String cql = builder.build().toString();

    Map<String, Object> parameters = Maps.newHashMap();
    Map<String, Object> props = Maps.newHashMap();
    props.put(SNPropertyKeys.age.name(), 29);
    props.put(SNPropertyKeys.email.name(), "bob@example.com");
    parameters.put("props", props);

    executeCypherQuery(db, cql, parameters);
  }

  public static void simple(GraphDatabaseService db) {
    CypherQueryBuilder builder = new CypherQueryBuilder();
    builder.MATCH("(n {name: 'John Johnson'})");
    builder.RETURN("n, n.name");
    // CQL和参数
    String cql = builder.build().toString();
    Map<String, Object> parameters = Maps.newHashMap();

    executeCypherQuery(db, cql, parameters);
  }

  @ShouldExecuteInTransaction
  private static void executeCypherQuery(final GraphDatabaseService db, final String cql,
      Map<String, Object> parameters) {
    Preconditions.checkArgument(db != null, "Argument db should not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(cql),
      "Argument cql should not be null or empty!");

    LOG.info("\n" + cql);
    // 执行查询
    if (MapUtils.isEmpty(parameters)) {
      parameters = Maps.newHashMap();
    }
    Result result = db.execute(cql, parameters);
    LOG.info("\n" + Neo4js.asString(result));
  }

}
