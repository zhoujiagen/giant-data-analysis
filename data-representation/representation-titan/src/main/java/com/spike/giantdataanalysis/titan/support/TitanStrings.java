package com.spike.giantdataanalysis.titan.support;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.VertexLabel;

/**
 * Titan实体的字符串表示工具类.
 */
public class TitanStrings {
  public static String repr(Vertex vertex) {
    Preconditions.checkArgument(vertex != null);

    StringBuilder sb = new StringBuilder();
    sb.append("Vertex[id=").append(vertex.id());
    sb.append(", label=").append(vertex.label());

    sb.append(", properties=(");
    sb.append(Joiner.on(",").join(vertex.properties()));
    sb.append(")");
    sb.append("]");

    // TODO(zhoujiagen) add outgoing edges

    return sb.toString();
  }

  public static String repr(Edge edge) {
    Preconditions.checkArgument(edge != null);

    StringBuilder sb = new StringBuilder();
    sb.append("Edge[id=").append(edge.id());
    sb.append(", ").append(edge.outVertex().id()).append("=>").append(edge.inVertex().id());
    sb.append(", label=").append(edge.label());
    sb.append(", properties=(");
    sb.append(Joiner.on(",").join(edge.properties()));
    sb.append(")");
    sb.append("]");

    return sb.toString();
  }

  public static String repr(EdgeLabel edgeLabel) {
    Preconditions.checkArgument(edgeLabel != null);

    StringBuilder sb = new StringBuilder();
    sb.append("EdgeLabel[id=").append(edgeLabel.id());
    sb.append(", name=").append(edgeLabel.name());
    sb.append(", multiplicity=").append(edgeLabel.multiplicity());
    sb.append(", isDirected=").append(edgeLabel.isDirected());
    sb.append(", isDirected=").append(edgeLabel.isDirected());
    sb.append("]");

    return sb.toString();
  }

  public static String repr(VertexLabel vertexLabel) {
    Preconditions.checkArgument(vertexLabel != null);

    StringBuilder sb = new StringBuilder();
    sb.append("VertexLabel[id=").append(vertexLabel.id());
    sb.append(", name=").append(vertexLabel.name());
    sb.append(", isPartitioned=").append(vertexLabel.isPartitioned());
    sb.append(", isStatic=").append(vertexLabel.isStatic());
    sb.append("]");

    return sb.toString();
  }

  public static String repr(PropertyKey propertyKey) {
    Preconditions.checkArgument(propertyKey != null);

    StringBuilder sb = new StringBuilder();
    sb.append("PropertyKey[id=").append(propertyKey.id());
    sb.append(", name=").append(propertyKey.name());
    sb.append(", dataType=").append(propertyKey.dataType());
    sb.append(", cardinality=").append(propertyKey.cardinality());
    sb.append("]");

    return sb.toString();
  }

  // catch left
  public static String repr(Object object) {
    Preconditions.checkArgument(object != null);

    return object.toString();
  }

}
