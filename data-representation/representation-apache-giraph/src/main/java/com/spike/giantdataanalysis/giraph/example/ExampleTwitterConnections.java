package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

/**
 * Twitter用户图中每个节点的关注/被关注边的数量
 */
public class ExampleTwitterConnections extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, Text> {

  public static void main(String[] args) throws Exception {

    args = new String[] { //
            "com.spike.giantdataanalysis.giraph.example.ExampleTwitterConnections", //
            // 输入
            "-vip",
            "src/main/resources/dataset/helloworld/TextDoubleDoubleAdjacencyListVertexInputFormat",//
            "-vif", "org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat",//
            // 输出
            "-op", "output",//
            "-vof", "org.apache.giraph.io.formats.AdjacencyListTextVertexOutputFormat",//
            "-w", "1",//
            "-ca", "giraph.SplitMasterWorker=false,giraph.logLevel=error" };

    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

  @Override
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex, Iterable<Text> messages)
      throws IOException {

    if (super.getSuperstep() == 0l) {

      vertex.setValue(new DoubleWritable(vertex.getNumEdges()));
      super.sendMessageToAllEdges(vertex, new Text());

    } else {

      int inDegree = 0;
      for (@SuppressWarnings("unused")
      Text message : messages) {
        inDegree++;
      }
      vertex.setValue(new DoubleWritable(vertex.getValue().get() + inDegree));

    }

    vertex.voteToHalt();
  }

}
