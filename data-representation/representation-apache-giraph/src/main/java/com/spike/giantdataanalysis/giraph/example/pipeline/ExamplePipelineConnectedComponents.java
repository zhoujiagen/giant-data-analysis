package com.spike.giantdataanalysis.giraph.example.pipeline;

import java.io.IOException;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 图的连通组件: 连通的组件中节点的值为最大节点ID.
 * @author zhoujiagen
 */
public class ExamplePipelineConnectedComponents extends
    BasicComputation<IntWritable, IntWritable, NullWritable, IntWritable> {

  @Override
  public void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    if (super.getSuperstep() == 0l) {
      super.sendMessageToAllEdges(vertex, vertex.getId());
    } else {
      int maxVertexId = vertex.getValue().get();
      for (IntWritable sourceVertexId : messages) {
        if (sourceVertexId.get() > maxVertexId) {
          maxVertexId = sourceVertexId.get();
        }
      }

      // 更新当前节点的值, 并广播这一变更
      if (maxVertexId > vertex.getValue().get()) {
        IntWritable newVertexValue = new IntWritable(maxVertexId);
        vertex.setValue(newVertexValue);
        super.sendMessageToAllEdges(vertex, newVertexValue);
      }
    }

    vertex.voteToHalt();
  }

}
