package com.spike.giantdataanalysis.titan.example;

import java.io.File;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.example.GraphOfTheGodsFactory;

/**
 * REF: http://s3.thinkaurelius.com/docs/titan/current/server.html
 * @author zhoujiagen
 * @see GraphOfTheGodsFactory
 */
public class GraphOfTheGodsExample {
  private static final Logger LOG = LoggerFactory.getLogger(GraphOfTheGodsExample.class);

  public static void main(String[] args) {
    System.out.println(new File(".").getAbsolutePath());
    TitanGraph graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");

    // load only once
    GraphOfTheGodsFactory.load(graph);

    GraphTraversalSource gts = graph.traversal();
    List<Object> names = gts.V().values("name").toList();

    LOG.info(names.toString());

    graph.close();
  }
}
