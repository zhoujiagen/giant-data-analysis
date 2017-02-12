package com.spike.giantdataanalysis.storm.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

/**
 * Titan工具类
 * @author zhoujiagen
 */
public final class Titans {
  // 配置
  // REF: http://s3.thinkaurelius.com/docs/titan/current/configuration.html
  // REF: http://s3.thinkaurelius.com/docs/titan/current/titan-config-ref.html
  public static final String storage_backend_key = "storage.backend";
  public static final String storage_hostname_key = "storage.hostname";
  public static final String storage_port_key = "storage.port";

  public interface GraphFactory extends Serializable {
    Graph make(Map<String, Object> conf);
  }

  public static class TitanGraphFactory implements GraphFactory {
    private static final long serialVersionUID = -4436664158416466570L;

    public TitanGraph make(Map<String, Object> conf) {
      Configuration graphConf = new BaseConfiguration();
      graphConf.setProperty(Titans.storage_backend_key, conf.get(Titans.storage_backend_key));
      graphConf.setProperty(Titans.storage_hostname_key, conf.get(Titans.storage_hostname_key));
      TitanGraph result = TitanFactory.open(graphConf);
      return result;
    }
  }

  /**
   * 创建节点
   * @param graph
   * @param label
   * @param properties
   * @return
   */
  public static Vertex createV(Graph graph, String label, Map<String, Object> properties) {
    Vertex v = null;
    if (label != null) {
      v = graph.addVertex(label);
    } else {
      v = graph.addVertex();
    }

    for (String propName : properties.keySet()) {
      v.property(propName, properties.get(propName));
    }

    return v;
  }

  /**
   * 创建边
   * @param graph
   * @param label
   * @param from
   * @param to
   * @param properties
   */
  public static void createE(Graph graph, String label, Vertex from, Vertex to,
      Map<String, Object> properties) {
    Edge edge = from.addEdge(label, to);
    for (String propName : properties.keySet()) {
      edge.property(propName, properties.get(propName));
    }
  }

  /**
   * 查找节点
   * @param graph
   * @param propName
   * @param propValue
   * @return
   */
  public static Vertex findV(Graph graph, String propName, String propValue) {
    GraphTraversalSource gts = graph.traversal();
    GraphTraversal<Vertex, Vertex> gt = gts.V().has(propName, propValue);
    List<Vertex> vertexs = gt.toList();
    if (vertexs != null && vertexs.size() > 0) {
      return vertexs.get(0);
    }
    return null;
  }
}
