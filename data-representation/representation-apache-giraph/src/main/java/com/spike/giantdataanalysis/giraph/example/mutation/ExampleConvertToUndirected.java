package com.spike.giantdataanalysis.giraph.example.mutation;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import com.spike.giantdataanalysis.giraph.example.ExampleTwitter2Facebook;

/**
 * 将有向图转换为无向图: 使用Vertex上方法直接修改图结构.
 * @author zhoujiagen
 * @see ExampleTwitter2Facebook
 */
public class ExampleConvertToUndirected extends
    BasicComputation<Text, DoubleWritable, NullWritable, Text> {

  @Override
  public void compute(Vertex<Text, DoubleWritable, NullWritable> vertex, Iterable<Text> messages)
      throws IOException {

    if (super.getSuperstep() == 0l) {
      super.sendMessageToAllEdges(vertex, vertex.getId());
    } else {
      for (Text message : messages) {
        // 边不存在则创建
        if (vertex.getEdgeValue(message) == null) {
          vertex.addEdge(EdgeFactory.create(message, NullWritable.get()));
        }
      }
    }

    vertex.voteToHalt();
  }

}
