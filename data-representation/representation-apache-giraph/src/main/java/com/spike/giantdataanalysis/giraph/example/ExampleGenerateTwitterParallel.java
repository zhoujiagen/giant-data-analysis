package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

/**
 * 生成Twitter图: 通过'发送消息给不存在的节点创建节点'机制.
 */
public class ExampleGenerateTwitterParallel extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, IntWritable> {

  /** 种子节点ID */
  public static final Text SEED_VERTEX_ID = new Text("seed");
  /** 默认的节点值 */
  public static final DoubleWritable DEFAULT_VERTEX_VALUE = new DoubleWritable(0);
  /** 默认的边值 */
  public static final DoubleWritable DEFAULT_EDGE_VALUE = new DoubleWritable(0);

  private static final String[] vertexIds = { //
      "", "John", "Peter", "Mark", "Anne", "Natalie", "Jack", "Julia" };
  private static final byte[][] vertexAdjEdges = { // 各值为在vertexIds的索引
      { 0 }, { 2 }, {}, { 1, 4 }, { 2, 7 }, { 1, 2, 4 }, { 3, 4 }, { 3, 5 } };

  // 非节点ID的标识
  private static final byte nonVertexIdIndicator = -1;

  /**
   * 将生成的三个文件拼接在一起, 执行: dot giraph.dot -Tpng -ogiraph.png
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    args = new String[] { //
        "com.spike.giantdataanalysis.giraph.example.ExampleGenerateTwitterParallel", //
            // 输入
            "-vip", "src/main/resources/dataset/helloworld/seed",//
            "-vif", "org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat",//
            // 输出
            "-op", "output",//
            "-vof", "org.apache.giraph.io.formats.GraphvizOutputFormat",//
            "-w", "1",//
            "-ca", "giraph.SplitMasterWorker=false,giraph.logLevel=error" };

    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

  @Override
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex,
      Iterable<IntWritable> messages) throws IOException {

    if (super.getSuperstep() == 0l) {

      // 只有种子节点
      for (int i = 1; i < vertexAdjEdges.length; i++) {
        Text destVertexId = new Text(vertexIds[i]);

        if (vertexAdjEdges[i].length > 0) {
          for (byte adjVertex : vertexAdjEdges[i]) {
            // 给节点发送其连接节点ID消息(当前节点不存在)
            super.sendMessage(destVertexId, new IntWritable(adjVertex));
          }
        } else {
          super.sendMessage(destVertexId, new IntWritable(nonVertexIdIndicator));
        }
      }

      // 移除种子节点
      super.removeVertexRequest(SEED_VERTEX_ID);

    } else {

      for (IntWritable message : messages) {
        if (message.get() == nonVertexIdIndicator) { // 这个不是连接节点ID
          continue;
        }

        Text adjVertexId = new Text(vertexIds[message.get()]);
        vertex.addEdge(EdgeFactory.create(adjVertexId, DEFAULT_EDGE_VALUE));
      }

      vertex.voteToHalt();
    }

  }
}
