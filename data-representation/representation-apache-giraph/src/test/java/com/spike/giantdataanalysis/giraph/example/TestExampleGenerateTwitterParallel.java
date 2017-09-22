package com.spike.giantdataanalysis.giraph.example;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.AdjacencyListTextVertexOutputFormat;
import org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat;
import org.apache.giraph.utils.InternalVertexRunner;
import org.junit.Assert;
import org.junit.Test;

/**
 * Giraph应用的单元测试: 生成示例Twitter图.
 */
public class TestExampleGenerateTwitterParallel {

  // 图输入
  private static final String[] GRAPH_INPUT = new String[] {//
      "seed\t0"//
      };
  // 预期的节点数量
  private static final int EXPECTED_VERTEX_COUNT = 7;

  @Test
  public void testVertexCount() {
    // Giraph配置
    GiraphConfiguration conf = new GiraphConfiguration();
    // 配置: 计算类
    conf.setComputationClass(ExampleGenerateTwitterParallel.class);
    // 配置: 节点输入格式类
    conf.setVertexInputFormatClass(TextDoubleDoubleAdjacencyListVertexInputFormat.class);
    // 配置: 节点输出格式类
    conf.setVertexOutputFormatClass(AdjacencyListTextVertexOutputFormat.class);

    // 使用内部节点执行器执行, 并获取输出
    Iterable<String> outputs = null;
    try {
      outputs = InternalVertexRunner.run(conf, GRAPH_INPUT); // 设置图输入
    } catch (Exception e) {
      e.printStackTrace();
    }

    int vertexCount = 0;
    for (String output : outputs) {
      System.err.println(output);
      vertexCount++;
    }

    Assert.assertEquals(EXPECTED_VERTEX_COUNT, vertexCount);

  }

}
