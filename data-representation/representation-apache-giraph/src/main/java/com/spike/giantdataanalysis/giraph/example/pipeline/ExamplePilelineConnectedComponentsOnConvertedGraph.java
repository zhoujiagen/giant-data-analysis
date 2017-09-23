package com.spike.giantdataanalysis.giraph.example.pipeline;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 按超级步编号组装管道: 有向图转无向图 => 连通组件
 * @author zhoujiagen
 */
public class ExamplePilelineConnectedComponentsOnConvertedGraph extends
    BasicComputation<IntWritable, IntWritable, NullWritable, IntWritable> {

  @Override
  public void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    // (1) ExamplePipelineConvertGraph
    if (super.getSuperstep() == 0l) {
      super.sendMessageToAllEdges(vertex, vertex.getId());
    } else if (super.getSuperstep() == 1l) {
      for (IntWritable sourceVertexId : messages) {
        if (vertex.getEdgeValue(sourceVertexId) == null) {
          vertex.addEdge(EdgeFactory.create(sourceVertexId, NullWritable.get()));
        }
      }
    }

    // (2) ExamplePipelineConnectedComponents
    else if (super.getSuperstep() == 2l) {
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
