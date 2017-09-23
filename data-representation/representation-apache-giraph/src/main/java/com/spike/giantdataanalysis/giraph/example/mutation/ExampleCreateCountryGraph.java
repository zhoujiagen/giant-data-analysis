package com.spike.giantdataanalysis.giraph.example.mutation;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

/**
 * 有用户的国家标签值创建国家节点图, 保持用户之间的边方向: 使用Computation中方法修改图结构.
 * @author zhoujiagen
 */
public class ExampleCreateCountryGraph extends BasicComputation<Text, Text, NullWritable, Text> {

  @Override
  public void compute(Vertex<Text, Text, NullWritable> vertex, Iterable<Text> messages)
      throws IOException {
    Text country = vertex.getValue(); // 节点的标签值: 国家

    if (super.getSuperstep() == 0l) {

      super.sendMessageToAllEdges(vertex, country);

    } else {

      for (Text message : messages) {
        // source country => my country
        super.addEdgeRequest(message, EdgeFactory.create(country, NullWritable.get()));
      }

      // 移除用户节点
      super.removeVertexRequest(vertex.getId());
      vertex.voteToHalt();
    }
  }

}
