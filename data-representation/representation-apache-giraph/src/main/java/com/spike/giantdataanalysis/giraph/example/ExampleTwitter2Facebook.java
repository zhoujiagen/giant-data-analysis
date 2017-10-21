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
 * 单向图转为无向图
 */
public class ExampleTwitter2Facebook extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, Text> {

  /** 原始边的值 */
  public static final DoubleWritable ORIGINAL_EDGE_VALUE = new DoubleWritable(1);
  /** 新增的边的值 */
  public static final DoubleWritable ADDED_EDGE_VALUE = new DoubleWritable(2);

  /**
   * 将生成的三个文件拼接在一起, 执行: dot giraph.dot -Tpng -ogiraph.png
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    args = new String[] { //
            "com.spike.giantdataanalysis.giraph.example.ExampleTwitter2Facebook", //
            // 输入
            "-vip",
            "src/main/resources/dataset/helloworld/TextDoubleDoubleAdjacencyListVertexInputFormat",//
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

      // 向连接边上节点发送消息: 自身节点ID
      super.sendMessageToAllEdges(vertex, vertex.getId());

    } else {

      for (Text sourceVertexId : messages) {
        DoubleWritable edgeValue = vertex.getEdgeValue(sourceVertexId);

        if (edgeValue == null) {
          // 创建边
          vertex.addEdge(EdgeFactory.create(sourceVertexId, ADDED_EDGE_VALUE));
        } else {
          // 设置原始边的值
          vertex.setEdgeValue(sourceVertexId, ORIGINAL_EDGE_VALUE);
        }
      }

    }

    vertex.voteToHalt();
  }

}
