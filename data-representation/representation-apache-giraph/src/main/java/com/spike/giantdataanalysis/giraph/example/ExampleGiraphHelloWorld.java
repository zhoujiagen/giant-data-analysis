package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.util.ToolRunner;

public class ExampleGiraphHelloWorld //
    extends BasicComputation<IntWritable, IntWritable, NullWritable, NullWritable> {

  public static void main(String[] args) throws Exception {

    // case 1: IntIntNullTextInputFormat
    args = new String[] { //
        "com.spike.giantdataanalysis.giraph.example.ExampleGiraphHelloWorld", //
            "-vip", "src/main/resources/dataset/helloworld/IntIntNullTextInputFormat",//
            "-vif", "org.apache.giraph.io.formats.IntIntNullTextInputFormat",//
            "-w", "1",//
            "-ca", "giraph.SplitMasterWorker=false,giraph.logLevel=error" };

    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

  @Override
  public void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex,
      Iterable<NullWritable> messages) throws IOException {
    System.out.print("Hello world from the: " + vertex.getId().toString() + " who is following: ");

    // 迭代节点的所有邻居
    for (Edge<IntWritable, NullWritable> e : vertex.getEdges()) {
      System.out.print(" " + e.getTargetVertexId());
    }
    System.out.println();

    // 当前节点的当前BSP计算结束
    vertex.voteToHalt();
  }

}
