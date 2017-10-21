package com.spike.giantdataanalysis.giraph.example;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.io.formats.TextDoubleDoubleAdjacencyListVertexInputFormat;
import org.apache.giraph.utils.InternalVertexRunner;
import org.junit.Test;

/**
 * 测试单源最短路径计算.
 */
public class TestExampleSingleSourceShortestPath {

  // 图输入
  private static final String[] GRAPH_INPUT = new String[] {//
      "Mark\t0\tJohn\t1\tAnne\t2",//
          "John\t0\tPeter\t2\tJulia\t3",//
          "Peter\t0\tMaria\t5",//
          "Julia\t0\tMaria\t2\tSophia\t2",//
          "Anne\t0\tSophia\t4\tTom\t1",//
          "Tom\t0\tSophia\t1",//
          "Maria\t0",//
          "Sophia\t0"//
      };

  /**
   * <pre>
   * Output:
   * 
   * Peter 3.0
   * Julia 4.0
   * Anne  2.0
   * Maria 6.0
   * Tom 3.0
   * Sophia  4.0
   * Mark  0.0
   * John  1.0
   * </pre>
   */
  @Test
  public void testVertexCount() {
    // Giraph配置
    GiraphConfiguration conf = new GiraphConfiguration();
    // 设置参数
    conf.set(ExampleSingleSourceShortestPath.CONFIG_SOURCE_ID, "Mark");

    // 配置: 计算类
    conf.setComputationClass(ExampleSingleSourceShortestPath.class);
    // 配置: 节点输入格式类
    conf.setVertexInputFormatClass(TextDoubleDoubleAdjacencyListVertexInputFormat.class);
    // 配置: 节点输出格式类
    conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);

    // 使用内部节点执行器执行, 并获取输出
    Iterable<String> outputs = null;
    try {
      outputs = InternalVertexRunner.run(conf, GRAPH_INPUT); // 设置图输入
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (String output : outputs) {
      System.err.println(output);
    }

  }
}
