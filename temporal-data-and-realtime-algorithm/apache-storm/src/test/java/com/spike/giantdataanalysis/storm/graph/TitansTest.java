package com.spike.giantdataanalysis.storm.graph;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.BaseConfiguration;

import com.spike.giantdataanalysis.storm.support.Titans;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;

public class TitansTest {
  public static void main(String[] args) {
    BaseConfiguration conf = new BaseConfiguration();
    conf.setProperty("storage.backend", "cassandra");
    conf.setProperty("storage.hostname", "localhost");
    TitanGraph graph = TitanFactory.open(conf);

    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("name", "zhoujiagen");
    TitanTransaction tx = graph.newTransaction();
    Titans.createV(graph, "user", properties);
    tx.commit();
  }
}
