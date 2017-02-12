package com.spike.giantdataanalysis.storm.graph;

import java.util.List;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseStateUpdater;
import storm.trident.tuple.TridentTuple;

import com.spike.giantdataanalysis.storm.graph.GraphStateFactory.GraphState;

public class GraphStateUpdater extends BaseStateUpdater<GraphState> {
  private static final long serialVersionUID = 1L;

  private GraphTupleProcessor graphTupleProcessor;

  public GraphStateUpdater(GraphTupleProcessor graphTupleProcessor) {
    this.graphTupleProcessor = graphTupleProcessor;
  }

  @Override
  public void updateState(GraphState state, List<TridentTuple> tuples, TridentCollector collector) {
    state.update(tuples, collector, this.graphTupleProcessor);
  }

}
