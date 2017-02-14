package com.spike.giantdataanalysis.titan.support;

import org.elasticsearch.common.Preconditions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

public class TitansTest {

  private static TitanGraph graph;

  @BeforeClass
  public static void beforeClass() {
    graph = TitanFactory.open("src/main/resources/conf/titan-cassandra-es.properties");
    Preconditions.checkNotNull(graph);
  }

  @AfterClass
  public static void afterClass() {
    graph.close();
  }

  @Test
  public void findV() {

  }

  @Test
  public void createV() {

  }

  @Test
  public void createE() {

  }
}
