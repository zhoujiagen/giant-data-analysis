package com.spike.giantdataanalysis.giraph.example.mutation;

import org.apache.giraph.conf.DefaultImmutableClassesGiraphConfigurable;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.giraph.graph.VertexChanges;
import org.apache.giraph.graph.VertexResolver;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * 通过节点解析器在边上设置添加边的请求数量.
 * @author zhoujiagen
 * @see org.apache.giraph.graph.DefaultVertexResolver<I, V, E>
 */
public class ExampleCountedAddEdgeRequestVertexResolver //
    extends DefaultImmutableClassesGiraphConfigurable<Text, Text, IntWritable> //
    implements VertexResolver<Text, Text, IntWritable> {

  @Override
  public Vertex<Text, Text, IntWritable> resolve(//
      Text vertexId,//
      Vertex<Text, Text, IntWritable> vertex, //
      VertexChanges<Text, Text, IntWritable> vertexChanges,//
      boolean hasMessages) {

    if (vertexChanges == null || vertexChanges.getAddedEdgeList().isEmpty()) {
      return vertex;
    }

    if (vertex == null) {
      vertex = super.getConf().createVertex();
      vertex.initialize(vertexId, super.getConf().createVertexValue());
    }

    for (Edge<Text, IntWritable> edge : vertexChanges.getAddedEdgeList()) {
      IntWritable edgeValue = vertex.getEdgeValue(edge.getTargetVertexId());
      if (edgeValue == null) {
        // add the edge
        vertex.addEdge(edge);
        // set edge value
        vertex.setEdgeValue(edge.getTargetVertexId(), new IntWritable(1));
      } else {
        // set edge value
        vertex.setEdgeValue(edge.getTargetVertexId(), new IntWritable(edgeValue.get() + 1));
      }
    }

    return vertex;
  }
}
