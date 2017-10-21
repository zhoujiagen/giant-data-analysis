package com.spike.giantdataanalysis.neo4j.example.procedure;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.harness.junit.Neo4jRule;

public class FullTextIndexTest {

  @Rule
  public Neo4jRule neo4jRule = new Neo4jRule().withProcedure(FullTextIndex.class);

  @Test
  public void indexing_finding() {
    Driver driver =
        GraphDatabase.driver(neo4jRule.boltURI(), Config.build().withoutEncryption().toConfig());

    Session session = driver.session();
    long nodeId = session.run("CREATE (p:User {name: 'Alice'}) RETURN id(p)")//
        .single().get(0).asLong();

    // 创建索引
    session.run("CALL example.index({id}, ['name'])", Values.parameters("id", nodeId));

    // 搜索
    StatementResult stmtResult = session.run("CALL example.search('User', 'name:Ali*')");
    long foundNodeId = stmtResult.single().get(0).asLong();
    Assert.assertEquals(nodeId, foundNodeId);
  }

}
