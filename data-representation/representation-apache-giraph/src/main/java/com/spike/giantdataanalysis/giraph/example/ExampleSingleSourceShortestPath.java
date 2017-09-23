package com.spike.giantdataanalysis.giraph.example;

import java.io.IOException;

import org.apache.giraph.conf.StrConfOption;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计算单源最短路径.
 * <p>
 * 见sssp.png.
 */
public class ExampleSingleSourceShortestPath extends
    BasicComputation<Text, DoubleWritable, DoubleWritable, DoubleWritable> {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleSingleSourceShortestPath.class);

  /** 开始节点的配置键 */
  public static final String CONFIG_SOURCE_ID = "ExampleSingleSourceShortestPath.sourceVertexId";

  /** 配置: 开始节点ID */
  public static final StrConfOption SOURCE_ID = new StrConfOption(CONFIG_SOURCE_ID, "John",
      "single source shortest path's source vertex id");

  // 节点的初始值
  private static final DoubleWritable INIT_VERTEX_VALUE = new DoubleWritable(Double.MAX_VALUE);

  /** @return 是否是开始节点 */
  private boolean isSource(Vertex<Text, ?, ?> vertex) {
    return SOURCE_ID.get(super.getConf()).equals(vertex.getId().toString());
  }

  @Override
  public void compute(Vertex<Text, DoubleWritable, DoubleWritable> vertex,
      Iterable<DoubleWritable> messages) throws IOException {

    if (super.getSuperstep() == 0l) {
      // 将节点值设置为Double.MAX_VALUE
      vertex.setValue(INIT_VERTEX_VALUE);
    }

    // 当前的最短距离
    double minDistance = this.isSource(vertex) ? 0d : Double.MAX_VALUE;
    for (DoubleWritable message : messages) {
      minDistance = Math.min(minDistance, message.get());
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Vertex[id={}, value={}] current min distance={}", vertex.getId(),
        vertex.getValue(), minDistance);
    }

    // 当前节点需要更新自身的最短距离
    if (minDistance < vertex.getValue().get()) {
      vertex.setValue(new DoubleWritable(minDistance));

      for (Edge<Text, DoubleWritable> edge : vertex.getEdges()) {
        double distance = minDistance + edge.getValue().get();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Vertex[{}] =={}==> Vertex[{}]", vertex.getId(), distance,
            edge.getTargetVertexId());
        }
        // 将变更通知相邻节点
        super.sendMessage(edge.getTargetVertexId(), new DoubleWritable(distance));
      }
    }

    vertex.voteToHalt();
  }

}
