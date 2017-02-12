package com.spike.giantdataanalysis.storm.graph;

import java.io.Serializable;

import org.apache.tinkerpop.gremlin.structure.Graph;

import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public interface GraphTupleProcessor extends Serializable {

  void process(Graph graph, TridentTuple tuple, TridentCollector collector);

}
