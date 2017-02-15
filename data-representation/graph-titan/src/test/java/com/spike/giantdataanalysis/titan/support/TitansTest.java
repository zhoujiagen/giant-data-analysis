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

public class TitansTest {
  private static final Logger LOG = LoggerFactory.getLogger(TitansTest.class);

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
    Vertex Anna = Titans.createV(graph, "Anna", null);
    Vertex Barbara = Titans.createV(graph, "Barbara", null);
    Vertex Carol = Titans.createV(graph, "Carol", null);
    Vertex Dawn = Titans.createV(graph, "Dawn", null);
    Vertex Elizabeth = Titans.createV(graph, "Elizabeth", null);
    Vertex Jill = Titans.createV(graph, "Jill", null);
    // company
    Vertex BigCo = Titans.createV(graph, "BigCo", null);
    // book and category
    Vertex Refactoring = Titans.createV(graph, "Refactoring", null);
    Vertex NoSQLDistilled = Titans.createV(graph, "NoSQLDistilled", null);
    Vertex DatabaseRefactoring = Titans.createV(graph, "DatabaseRefactoring", null);
    Vertex Databases = Titans.createV(graph, "Databases", null);
    // author
    Vertex Martin = Titans.createV(graph, "Martin", null);
    Vertex Pramod = Titans.createV(graph, "Pramod", null);

    // relations
    String employee = "employee";
    String friend = "friend";
    String likes = "likes";
    String author = "author";
    String category = "category";
    Titans.createE(graph, employee, Anna, BigCo, null);
    Titans.createE(graph, employee, Barbara, BigCo, null);
    Titans.createE(graph, employee, Carol, BigCo, null);

    Titans.createE(graph, friend, Barbara, Anna, null);
    Titans.createE(graph, friend, Barbara, Carol, null);
    Titans.createE(graph, friend, Carol, Dawn, null);
    Titans.createE(graph, friend, Dawn, Jill, null);
    Titans.createE(graph, friend, Barbara, Elizabeth, null);
    Titans.createE(graph, friend, Elizabeth, Jill, null);

    Titans.createE(graph, likes, Anna, Refactoring, null);
    Titans.createE(graph, likes, Barbara, Refactoring, null);
    Titans.createE(graph, likes, Barbara, NoSQLDistilled, null);
    Titans.createE(graph, likes, Carol, NoSQLDistilled, null);
    Titans.createE(graph, likes, Dawn, NoSQLDistilled, null);
    Titans.createE(graph, likes, Elizabeth, NoSQLDistilled, null);

    Titans.createE(graph, category, NoSQLDistilled, Databases, null);
    Titans.createE(graph, category, DatabaseRefactoring, Databases, null);

    Titans.createE(graph, author, Refactoring, Martin, null);
    Titans.createE(graph, author, NoSQLDistilled, Martin, null);
    Titans.createE(graph, author, NoSQLDistilled, Pramod, null);
    Titans.createE(graph, author, DatabaseRefactoring, Pramod, null);

    tx.commit();
  }

  private static void _clean() throws BackendException {
    graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");
    graph.close();
    Titans.clean(graph);
  }

}
