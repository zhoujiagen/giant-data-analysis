package com.spike.giantdataanalysis.giraph.example.aggregator;

import org.apache.giraph.aggregators.BasicAggregator;
import org.apache.hadoop.io.IntWritable;

public class ExampleIntSumAggragator extends BasicAggregator<IntWritable> {

  @Override
  public void aggregate(IntWritable value) {
    super.getAggregatedValue().set(super.getAggregatedValue().get() + value.get());
  }

  @Override
  public IntWritable createInitialValue() {
    return new IntWritable(0);
  }

}
