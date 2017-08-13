package com.spike.giantdataanalysis.neo4j.example.driver;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import com.google.common.collect.Maps;

/**
 * 使用{@link Driver}的示例.
 * <p>
 * REF 'neo4j-developer-manual-3.2-java'.
 */
public class Neo4jDriverExample {

  public static void main(String[] args) {
    // 使用了Bolt协议
    String uri = "bolt://127.0.0.1:7687/";
    String username = "neo4j";
    String password = "zhoujiagen";

    // 组装Cypher语句
    final StringBuilder d = new StringBuilder();
    d.append("MATCH (a:Greeting) \n");
    d.append("DELETE a \n");
    d.append("RETURN a");

    final StringBuilder w = new StringBuilder();
    w.append("CREATE (a:Greeting) \n");
    w.append("SET a.message = $message \n");
    w.append("RETURN a.message + ', from node ' + id(a)");

    final StringBuilder r = new StringBuilder();
    r.append("MATCH (a:Greeting) \n");
    r.append("RETURN a");

    // 开启会话, 执行事务
    try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password)); //
        Session session = driver.session();) {
      // 删除
      List<Record> deleteResult = session.writeTransaction(//
          new TransactionWork<List<Record>>() {
            @Override
            public List<Record> execute(Transaction tx) {
              StatementResult stmtResult = tx.run(d.toString());
              return stmtResult.list();
            }
          });
      System.out.println(deleteResult);

      // 写
      String writeResult = session.writeTransaction(//
          new TransactionWork<String>() {
            @Override
            public String execute(Transaction tx) {
              Map<String, Object> parameters = Maps.newHashMap();
              parameters.put("message", "Hello Neo4j!");
              StatementResult result = tx.run(w.toString(), parameters);
              return result.single().get(0).asString();
            }
          });

      System.out.println(writeResult);

      // 读
      List<Record> readResult = session.readTransaction(//
          new TransactionWork<List<Record>>() {
            @Override
            public List<Record> execute(Transaction tx) {
              StatementResult stmtResult = tx.run(r.toString());
              return stmtResult.list();
            }
          });
      System.out.println(readResult);
    }
  }
}
