package com.spike.giantdataanalysis.titan.support;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.elasticsearch.common.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.diskstorage.BackendException;

public class TitanOpsTest {
  private static final Logger LOG = LoggerFactory.getLogger(TitanOpsTest.class);

  private static TitanGraph graph;
  private static GraphTraversalSource g;

  public static void main(String[] args) throws BackendException {
    _clean();

    graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");
    Preconditions.checkNotNull(graph);
    _create_nosql_destailed_graph();

    g = graph.traversal();
    List<Object> names = g.V().values("name").toList();
    LOG.info(names.toString());

    graph.close();
  }

  private static void _create_nosql_destailed_graph() {
    TitanTransaction tx = graph.newTransaction(); // 在事务中

    // person
    Vertex Anna = TitanOps.createV(graph, "Anna", null);
    Vertex Barbara = TitanOps.createV(graph, "Barbara", null);
    Vertex Carol = TitanOps.createV(graph, "Carol", null);
    Vertex Dawn = TitanOps.createV(graph, "Dawn", null);
    Vertex Elizabeth = TitanOps.createV(graph, "Elizabeth", null);
    Vertex Jill = TitanOps.createV(graph, "Jill", null);
    // company
    Vertex BigCo = TitanOps.createV(graph, "BigCo", null);
    // book and category
    Vertex Refactoring = TitanOps.createV(graph, "Refactoring", null);
    Vertex NoSQLDistilled = TitanOps.createV(graph, "NoSQLDistilled", null);
    Vertex DatabaseRefactoring = TitanOps.createV(graph, "DatabaseRefactoring", null);
    Vertex Databases = TitanOps.createV(graph, "Databases", null);
    // author
    Vertex Martin = TitanOps.createV(graph, "Martin", null);
    Vertex Pramod = TitanOps.createV(graph, "Pramod", null);

    // relations
    String employee = "employee";
    String friend = "friend";
    String likes = "likes";
    String author = "author";
    String category = "category";
    TitanOps.createE(graph, employee, Anna, BigCo, null);
    TitanOps.createE(graph, employee, Barbara, BigCo, null);
    TitanOps.createE(graph, employee, Carol, BigCo, null);

    TitanOps.createE(graph, friend, Barbara, Anna, null);
    TitanOps.createE(graph, friend, Barbara, Carol, null);
    TitanOps.createE(graph, friend, Carol, Dawn, null);
    TitanOps.createE(graph, friend, Dawn, Jill, null);
    TitanOps.createE(graph, friend, Barbara, Elizabeth, null);
    TitanOps.createE(graph, friend, Elizabeth, Jill, null);
    TitanOps.createE(graph, friend, Martin, Pramod, null);
    TitanOps.createE(graph, friend, Pramod, Martin, null);

    TitanOps.createE(graph, likes, Anna, Refactoring, null);
    TitanOps.createE(graph, likes, Barbara, Refactoring, null);
    TitanOps.createE(graph, likes, Barbara, NoSQLDistilled, null);
    TitanOps.createE(graph, likes, Carol, NoSQLDistilled, null);
    TitanOps.createE(graph, likes, Dawn, NoSQLDistilled, null);
    TitanOps.createE(graph, likes, Elizabeth, NoSQLDistilled, null);

    TitanOps.createE(graph, category, NoSQLDistilled, Databases, null);
    TitanOps.createE(graph, category, DatabaseRefactoring, Databases, null);

    TitanOps.createE(graph, author, Refactoring, Martin, null);
    TitanOps.createE(graph, author, NoSQLDistilled, Martin, null);
    TitanOps.createE(graph, author, NoSQLDistilled, Pramod, null);
    TitanOps.createE(graph, author, DatabaseRefactoring, Pramod, null);

    tx.commit();
  }

  private static void _clean() throws BackendException {
    graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");
    graph.close();
    TitanOps.clean(graph);
  }

}
