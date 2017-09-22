package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

public class ExampleGiraphHelloWorldV2 //
    extends BasicComputation<Text, DoubleWritable, DoubleWritable, NullWritable> {

  public static void main(String[] args) throws Exception {

    // case 2: TextDoubleDoubleAdjacencyListVertexInputFormat
    args = new String[] { //
            "com.spike.giantdataanalysis.giraph.example.ExampleGiraphHelloWorldV2", //
            "-vip",
            "src/main/resources/dataset/helloworld/TextDoubleDoubleAdjacencyListVertexInputFormat",//
            "-vif", "org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat",//
            "-w", "1",//
            "-ca", "giraph.SplitMasterWorker=false,giraph.logLevel=error" };

    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

  @Override
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex,
      Iterable<NullWritable> messages) throws IOException {
    System.out.print("Hello world from the: " + vertex.getId().toString() + " who is following: ");

    // 迭代节点的所有邻居
    for (Edge<Text, DoubleWritable> e : vertex.getEdges()) {
      System.out.print(" " + e.getTargetVertexId());
    }
    System.out.println();

    // 当前节点的当前BSP计算结束
    vertex.voteToHalt();
  }

}
