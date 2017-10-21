package com.spike.giantdataanalysis.neo4j.example.graphviz;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.visualization.graphviz.GraphStyle;
import org.neo4j.visualization.graphviz.MyDefaultStyleConfiguration;
import org.neo4j.visualization.graphviz.MyGraphvizWriter;
import org.neo4j.visualization.graphviz.MyNodeStyle;
import org.neo4j.visualization.graphviz.MyRelationshipStyle;
import org.neo4j.walk.Walker;

import com.spike.giantdataanalysis.neo4j.supports.Neo4js;
import com.spike.giantdataanalysis.neo4j.supports.annotation.ExecuteInTransaction;

/**
 * <pre>
 * 生成Neo4j图数据库的graphviz图.
 * REF: http://www.programcreek.com/java-api-examples/index.php?api=org.neo4j.visualization.graphviz.GraphvizWriter
 * 
 * 编译.dot: dot Neo.dot -Tpng -o Neo.png
 * 
 * Another options: 
 * nidi3/graphviz-java: Use graphviz with pure java.
 * </pre>
 */
public class GraphvizWriterTest {

  // http://www.programcreek.com/java-api-examples/index.php?api=org.neo4j.visualization.graphviz.GraphvizWriter
  @Test
  @ExecuteInTransaction
  public void graphviz() throws IOException {
    GraphDatabaseService db = Neo4js.defaultDatabase();

    try (Transaction tx = db.beginTx();) {
      // 风格配置
      MyDefaultStyleConfiguration styleConfiguration = new MyDefaultStyleConfiguration();
      // 节点风格
      MyNodeStyle nodeStyle = new MyNodeStyle(styleConfiguration);
      // 关系风格
      MyRelationshipStyle relationshipStyle = new MyRelationshipStyle(styleConfiguration);
      // 图风格
      GraphStyle graphStyle = new GraphStyle(nodeStyle, relationshipStyle);
      // graphviz输出
      MyGraphvizWriter writer = new MyGraphvizWriter(graphStyle);
      // 图遍历
      Walker walker = Walker.fullGraph(db);
      writer.emit(new File("graphviz/Neo.dot"), walker);

      tx.close();
    }

    db.shutdown();
  }
}
