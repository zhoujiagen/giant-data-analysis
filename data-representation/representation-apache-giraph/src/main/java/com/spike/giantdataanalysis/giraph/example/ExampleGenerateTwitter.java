package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

/**
 * 生成Twitter图: 由种子节点输入生成.
 */
public class ExampleGenerateTwitter extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, Text> {

  /** 种子节点ID */
  public static final Text SEED_VERTEX_ID = new Text("seed");
  /** 默认的节点值 */
  public static final DoubleWritable DEFAULT_VERTEX_VALUE = new DoubleWritable(0);
  /** 默认的边值 */
  public static final DoubleWritable DEFAULT_EDGE_VALUE = new DoubleWritable(0);

  // 节点ID
  private static final String[] vertexIds = { //
      "seed", "John", "Peter", "Mark", "Anne", "Natalie", "Jack", "Julia" };
  // 节点的连接节点
  private static final byte[][] vertexAdjEdges = { // 各值为在vertexIds的索引
      { 0 }, { 2 }, {}, { 1, 4 }, { 2, 7 }, { 1, 2, 4 }, { 3, 4 }, { 3, 5 } };

  /**
   * 将生成的三个文件拼接在一起, 执行: dot giraph.dot -Tpng -ogiraph.png
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    args = new String[] { //
        "com.spike.giantdataanalysis.giraph.example.ExampleGenerateTwitter", //
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
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex, Iterable<Text> messages)
      throws IOException {

    if (super.getSuperstep() == 0l) {

      // 只有种子节点
      for (int i = 1; i < vertexIds.length; i++) {
        Text destVertexId = new Text(vertexIds[i]);
        // 添加节点请求
        super.addVertexRequest(destVertexId, DEFAULT_VERTEX_VALUE);

        for (byte adjVertex : vertexAdjEdges[i]) {
          // 添加边请求
          super.addEdgeRequest(destVertexId, //
            EdgeFactory.create(new Text(vertexIds[adjVertex]), DEFAULT_EDGE_VALUE));
        }
      }

      // 移除种子节点
      super.removeVertexRequest(SEED_VERTEX_ID);

    } else {

      vertex.voteToHalt();
    }

  }

}
