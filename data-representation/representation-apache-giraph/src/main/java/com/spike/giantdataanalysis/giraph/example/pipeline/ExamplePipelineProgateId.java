package com.spike.giantdataanalysis.giraph.example.pipeline;

import java.io.IOException;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 传播节点ID.
 * @author zhoujiagen
 */
public class ExamplePipelineProgateId extends
    BasicComputation<IntWritable, NullWritable, NullWritable, IntWritable> {

  @Override
  public void compute(Vertex<IntWritable, NullWritable, NullWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    super.sendMessageToAllEdges(vertex, vertex.getId());
    vertex.voteToHalt();
  }

}
