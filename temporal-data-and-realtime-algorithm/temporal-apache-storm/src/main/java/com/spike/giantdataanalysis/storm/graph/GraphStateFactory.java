package com.spike.giantdataanalysis.storm.graph;

import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.structure.Graph;

import storm.trident.operation.TridentCollector;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.tuple.TridentTuple;
import backtype.storm.task.IMetricsContext;

import com.spike.giantdataanalysis.storm.support.Titans.GraphFactory;
import com.thinkaurelius.titan.core.TitanTransaction;

public class GraphStateFactory implements StateFactory {
  private static final long serialVersionUID = -8042636561985413646L;

  private GraphFactory graphFactory;

  public GraphStateFactory(GraphFactory graphFactory) {
    this.graphFactory = graphFactory;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
    GraphState graphState = new GraphState(this.graphFactory.make(conf));
    return graphState;
  }

  // /
  public static class GraphState implements State {

    private Graph graph;

    public GraphState(Graph graph) {
      this.graph = graph;
    }

    @Override
    public void beginCommit(Long txid) {
      // do nothing
    }

    @Override
    public void commit(Long txid) {
      // 提交事务
      if (this.graph instanceof TitanTransaction) {
        ((TitanTransaction) this.graph).commit();
      }
    }

    // 处理Storm tuple到图模型的转换
    public void update(List<TridentTuple> tuples, TridentCollector collector,
        GraphTupleProcessor processor) {
      for (TridentTuple tuple : tuples) {
        processor.process(this.graph, tuple, collector);
      }
    }
  }
}
