package com.spike.giantdataanalysis.titan.support;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.google.common.base.Preconditions;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.RelationType;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.VertexLabel;
import com.thinkaurelius.titan.core.schema.EdgeLabelMaker;
import com.thinkaurelius.titan.core.schema.PropertyKeyMaker;
import com.thinkaurelius.titan.core.schema.RelationTypeIndex;
import com.thinkaurelius.titan.core.schema.SchemaAction;
import com.thinkaurelius.titan.core.schema.TitanGraphIndex;
import com.thinkaurelius.titan.core.schema.TitanIndex;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.thinkaurelius.titan.core.schema.TitanManagement.IndexBuilder;
import com.thinkaurelius.titan.core.schema.VertexLabelMaker;

/**
 * Titan中图Schema操作工具类.
 * <p>
 * 基本上是{@link TitanManagement}的封装.
 */
public abstract class TitanSchemaOps {
  /** 默认的属性名称. */
  public static String DEFAULT_PROPERTY_NAME = "name";
  /** 默认的Label名称 */
  public static String DEFAULT_LABEL_NAME = Vertex.DEFAULT_LABEL;
  /** 默认的后端索引的索引名称 */
  public static String DEFAULT_INDEX_BACKEND_NAME = "search";

  /**
   * 获取{@link TitanGraph}的管理客户端.
   * @param graph
   * @return
   */
  public static TitanManagement openManagement(final TitanGraph graph) {
    Preconditions.checkArgument(graph != null);

    return graph.openManagement();
  }

  /**
   * 创建属性键Maker.
   * @param graph
   * @param propertyName
   * @return
   * @see PropertyKeyMaker#dataType(Class)
   * @see PropertyKeyMaker#cardinality(com.thinkaurelius.titan.core.Cardinality)
   * @see PropertyKeyMaker#signature(PropertyKey...)
   */
  public static PropertyKeyMaker makePropertyKey(final TitanGraph graph, final String propertyName) {
    Preconditions.checkArgument(graph != null);
    Preconditions.checkArgument(propertyName != null);

    return makePropertyKey(openManagement(graph), propertyName);
  }

  /**
   * 创建属性键.
   * @param graph
   * @param propertyName
   * @return
   * @see PropertyKeyMaker#dataType(Class)
   * @see PropertyKeyMaker#cardinality(com.thinkaurelius.titan.core.Cardinality)
   * @see PropertyKeyMaker#signature(PropertyKey...)
   */
  public static PropertyKeyMaker
      makePropertyKey(final TitanManagement tm, final String propertyName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(propertyName != null);

    return tm.makePropertyKey(propertyName);
  }

  /**
   * 获取中的属性键.
   * @param tm
   * @param propertyName
   * @return
   */
  public static PropertyKey getPropertyKey(final TitanManagement tm, final String propertyName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(propertyName != null);

    return tm.getPropertyKey(propertyName);
  }

  /**
   * 获取或创建属性键.
   * @param tm
   * @param propertyName
   * @return
   */
  public static PropertyKey getOrCreatePropertyKey(final TitanManagement tm,
      final String propertyName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(propertyName != null);

    return tm.getOrCreatePropertyKey(propertyName);
  }

  /**
   * 判断属性键是否存在.
   * @param tm
   * @param propertyName
   * @return
   */
  public static boolean containsPropertyKey(final TitanManagement tm, final String propertyName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(propertyName != null);

    return tm.containsPropertyKey(propertyName);
  }

  /**
   * 创建节点标签Maker.
   * @param tm
   * @param labelName
   * @return
   * @see VertexLabelMaker#partition()
   * @see VertexLabelMaker#setStatic()
   */
  public static VertexLabelMaker makeVertexLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.makeVertexLabel(labelName);
  }

  /**
   * 获取节点标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static VertexLabel getVertexLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.getVertexLabel(labelName);
  }

  /**
   * 获取或创建节点标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static VertexLabel
      getOrCreateVertexLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.getOrCreateVertexLabel(labelName);
  }

  /**
   * 判断是否包含节点标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static boolean containsVertexLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.containsVertexLabel(labelName);
  }

  /**
   * 创建边标签.
   * @param tm
   * @param labelName
   * @return
   * @see EdgeLabelMaker#multiplicity(com.thinkaurelius.titan.core.Multiplicity)
   * @see EdgeLabelMaker#directed()
   * @see EdgeLabelMaker#unidirected()
   */
  public static EdgeLabelMaker makeEdgeLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.makeEdgeLabel(labelName);
  }

  /**
   * 获取边标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static EdgeLabel getEdgeLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.getEdgeLabel(labelName);
  }

  /**
   * 获取或创建边标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static EdgeLabel getOrCreateEdgeLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.getOrCreateEdgeLabel(labelName);
  }

  /**
   * 判断是否包含边标签.
   * @param tm
   * @param labelName
   * @return
   */
  public static boolean containsEdgeLabel(final TitanManagement tm, final String labelName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(labelName != null);

    return tm.containsEdgeLabel(labelName);
  }

  /**
   * 判断索引是否存在.
   * @param tm
   * @param indexName
   * @return
   */
  public static boolean containsGraphIndex(final TitanManagement tm, final String indexName) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(indexName != null);

    return tm.containsGraphIndex(indexName);
  }

  /**
   * 判断关系类型的索引是否存在.
   * <p>
   * 关系类型: 边标签或者属性键.
   * @param tm
   * @param relationType EdgeLabel或PropertyKey.
   * @param edgeLabelOrPropertyKey
   * @return
   */
  public static boolean containsRelationIndex(final TitanManagement tm,
      final RelationType relationType, final String edgeLabelOrPropertyKey) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(relationType != null);
    Preconditions.checkArgument(edgeLabelOrPropertyKey != null);

    return tm.containsRelationIndex(relationType, edgeLabelOrPropertyKey);
  }

  /**
   * 判断关系类型是否存在.
   * <p>
   * 关系类型: 边标签或者属性键.
   * @param tm
   * @param edgeLabelOrPropertyKey
   * @return
   */
  public static boolean containsRelationType(final TitanManagement tm,
      final String edgeLabelOrPropertyKey) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(edgeLabelOrPropertyKey != null);

    return tm.containsRelationType(edgeLabelOrPropertyKey);
  }

  /**
   * 创建复合索引.
   * @param tm
   * @param indexName
   * @param clazz
   * @param unique 是否是唯一性索引.
   * @param unique
   * @param propertyKey
   * @return
   * @see IndexBuilder#addKey(PropertyKey, com.thinkaurelius.titan.core.schema.Parameter...)
   */
  public static TitanGraphIndex createCompositeIndex(final TitanManagement tm,
      final String indexName, final Class<? extends Element> clazz, final boolean unique,
      final PropertyKey propertyKey) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(indexName != null);
    Preconditions.checkArgument(!Vertex.class.equals(clazz) && !Edge.class.equals(clazz),
      "Currently only support Vertex.class or Edge.class");
    Preconditions.checkArgument(propertyKey != null);

    IndexBuilder indexBuilder = tm.buildIndex(indexName, clazz);
    if (unique) {
      indexBuilder = indexBuilder.unique();
    }

    return indexBuilder.addKey(propertyKey).buildCompositeIndex();
  }

  /**
   * 创建混合索引.
   * @param tm
   * @param indexName 索引名称
   * @param clazz
   * @param propertyKey
   * @param backingIndex 索引后端中的索引名称
   * @return
   * @see IndexBuilder#addKey(PropertyKey, com.thinkaurelius.titan.core.schema.Parameter...)
   */
  public static TitanGraphIndex createMixedIndex(final TitanManagement tm, final String indexName,
      Class<? extends Element> clazz, PropertyKey propertyKey, String backingIndex) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(indexName != null);
    Preconditions.checkArgument(!Vertex.class.equals(clazz) && !Edge.class.equals(clazz),
      "Currently only support Vertex.class or Edge.class");
    Preconditions.checkArgument(propertyKey != null);
    Preconditions.checkArgument(propertyKey.dataType() != null, "必须指定属性键值的数据类型!");

    return tm.buildIndex(indexName, clazz).addKey(propertyKey).buildMixedIndex(backingIndex);
  }

  /**
   * 创建节点视角的索引.
   * @param tm
   * @param edgeLabel
   * @param indexName
   * @param direction 为null时设置为Direction.OUT
   * @param sortOrder 为null时设置为Order.incr
   * @param sortKeys
   * @return
   */
  public static RelationTypeIndex createEdgeIndex(final TitanManagement tm,
      final EdgeLabel edgeLabel, final String indexName, Direction direction, Order sortOrder,
      final PropertyKey... sortKeys) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(edgeLabel != null);
    Preconditions.checkArgument(indexName != null);
    Preconditions.checkArgument(sortKeys != null && sortKeys.length > 0);

    if (direction == null) direction = Direction.OUT;
    if (sortOrder == null) sortOrder = Order.incr;

    return tm.buildEdgeIndex(edgeLabel, indexName, direction, sortOrder, sortKeys);
  }

  /**
   * 更新索引.
   * @param tm
   * @param index
   * @param updateAction
   */
  public static void updateIndex(final TitanManagement tm, final TitanIndex index,
      final SchemaAction updateAction) {
    Preconditions.checkArgument(tm != null);
    Preconditions.checkArgument(index != null);
    Preconditions.checkArgument(updateAction != null);

    tm.updateIndex(index, updateAction);
  }

}
