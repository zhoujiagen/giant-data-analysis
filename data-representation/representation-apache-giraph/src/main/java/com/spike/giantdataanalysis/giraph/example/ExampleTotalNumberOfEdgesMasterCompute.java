package com.spike.giantdataanalysis.giraph.example;

import org.apache.giraph.aggregators.LongSumAggregator;
import org.apache.giraph.master.DefaultMasterCompute;

/**
 * 计算边的总数的Master计算.
 */
public class ExampleTotalNumberOfEdgesMasterCompute extends DefaultMasterCompute {

  /** 聚合器的ID */
  public static final String AGGREGATOR_ID = ExampleTotalNumberOfEdges.class.getSimpleName();

  @Override
  public void compute() {
    System.err.println("超级步[" + super.getSuperstep() + "], 边的总数["
        + super.getAggregatedValue(AGGREGATOR_ID) + "]");
  }

  @Override
  public void initialize() throws InstantiationException, IllegalAccessException {
    // 注册聚合器
    super.registerAggregator(AGGREGATOR_ID, LongSumAggregator.class);
  }
}
