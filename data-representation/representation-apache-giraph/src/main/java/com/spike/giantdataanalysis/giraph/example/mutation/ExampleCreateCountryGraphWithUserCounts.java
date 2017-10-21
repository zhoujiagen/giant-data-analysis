package com.spike.giantdataanalysis.giraph.example.mutation;

import java.io.IOException;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * 有用户的国家标签值创建国家节点图, 保持用户之间的边方向和用户连接的数量: 使用向不存在的节点发送消息隐式创建节点方法.
 * @author zhoujiagen
 */
public class ExampleCreateCountryGraphWithUserCounts extends
    BasicComputation<Text, Text, IntWritable, Text> {

  @Override
  public void compute(Vertex<Text, Text, IntWritable> vertex, Iterable<Text> messages)
      throws IOException {
    // current vertex value: country
    Text country = vertex.getValue();

    if (super.getSuperstep() == 0l) {
      super.sendMessageToAllEdges(vertex, country);
    } else if (super.getSuperstep() == 1l) {
      for (Text message : messages) {
        // create country vertex
        super.sendMessage(message, country);
        // remove user vertex
        super.removeVertexRequest(vertex.getId());
        vertex.voteToHalt();
      }
    } else {
      for (Text message : messages) {
        // user1/country1 -(follow)-> user2/country2
        IntWritable edgeValue = vertex.getEdgeValue(message);
        if (edgeValue == null) {
          vertex.addEdge(EdgeFactory.create(message, new IntWritable(1)));
        } else {
          vertex.setEdgeValue(message, new IntWritable(edgeValue.get() + 1));
        }
      }
      vertex.voteToHalt();
    }

  }
}
