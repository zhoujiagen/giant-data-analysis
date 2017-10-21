package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

/**
 * 使用聚合器计算边的总数.
 */
public class ExampleTotalNumberOfEdges extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, NullWritable> {

  public static void main(String[] args) throws Exception {

    args = new String[] { //
            "com.spike.giantdataanalysis.giraph.example.ExampleTotalNumberOfEdges", //
            // Master计算
            "-mc",
            "com.spike.giantdataanalysis.giraph.example.ExampleTotalNumberOfEdgesMasterCompute",//
            // 聚合器的输出类
            "-aw",
            "org.apache.giraph.aggregators.TextAggregatorWriter",//
            // 聚合器的参数
            "-ca",
            "giraph.textAggregatorWriter.frequency=1,giraph.SplitMasterWorker=false,giraph.logLevel=error",//
            // 输入
            "-vip",
            "src/main/resources/dataset/helloworld/TextDoubleDoubleAdjacencyListVertexInputFormat",//
            "-vif", "org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat",//
            "-w", "1" };

    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

  @Override
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex,
      Iterable<NullWritable> messages) throws IOException {

    // 聚合
    super.aggregate(ExampleTotalNumberOfEdgesMasterCompute.AGGREGATOR_ID,
      new LongWritable(vertex.getNumEdges()));

    vertex.voteToHalt();
  }

}
