package com.spike.giantdataanalysis.giraph.example.pipeline;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 将有向图转换为无向图.
 * @author zhoujiagen
 */
public class ExamplePipelineConvertGraph extends
    BasicComputation<IntWritable, NullWritable, NullWritable, IntWritable> {

  @Override
  public void compute(Vertex<IntWritable, NullWritable, NullWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    if (super.getSuperstep() == 0l) {
      super.sendMessageToAllEdges(vertex, vertex.getId());
      vertex.voteToHalt();
    } else {
      for (IntWritable sourceVertexId : messages) {
        if (vertex.getEdgeValue(sourceVertexId) == null) {
          vertex.addEdge(EdgeFactory.create(sourceVertexId, NullWritable.get()));
        }
      }
      vertex.voteToHalt();
    }

  }

}
