package com.spike.giantdataanalysis.giraph.example.pipeline;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 添加逆向边.
 * @author zhoujiagen
 */
public class ExamplePipelineAddReverseEdge extends
    BasicComputation<IntWritable, NullWritable, NullWritable, IntWritable> {

  @Override
  public void compute(Vertex<IntWritable, NullWritable, NullWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    // 对接收到的消息
    for (IntWritable sourceVertexId : messages) {
      if (vertex.getEdgeValue(sourceVertexId) == null) {
        vertex.addEdge(EdgeFactory.create(sourceVertexId));
      }
    }

    vertex.voteToHalt();
  }

}
