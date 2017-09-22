package com.spike.giantdataanalysis.giraph.example.combiner;

import org.apache.giraph.combiner.MessageCombiner;
import org.apache.hadoop.io.IntWritable;

/**
 * 位数组组合器
 */
public class ExampleBitArrayCombiner implements MessageCombiner<IntWritable, IntWritable> {

  @Override
  public void combine(IntWritable vertexIndex, IntWritable originalMessage,
      IntWritable messageToCombine) {
    originalMessage.set((1 << messageToCombine.get()) | originalMessage.get());
  }

  @Override
  public IntWritable createInitialMessage() {
    return new IntWritable(0);
  }

}
