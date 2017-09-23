package com.spike.giantdataanalysis.giraph.example.aggregator;

import org.apache.giraph.aggregators.BasicAggregator;
import org.apache.hadoop.io.BooleanWritable;

/**
 * 布尔或聚合器
 * @author zhoujiagen
 */
public class ExampleBooleanOrAggregator extends BasicAggregator<BooleanWritable> {

  @Override
  public void aggregate(BooleanWritable value) {
    boolean currentValue = super.getAggregatedValue().get();
    boolean newValue = currentValue || value.get();// 布尔或的逻辑
    super.getAggregatedValue().set(newValue);
  }

  @Override
  public BooleanWritable createInitialValue() {
    return new BooleanWritable(false);
  }

}
