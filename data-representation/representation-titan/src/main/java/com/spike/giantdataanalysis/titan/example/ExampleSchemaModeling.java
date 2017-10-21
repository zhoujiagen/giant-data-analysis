package com.spike.giantdataanalysis.titan.example;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.spike.giantdataanalysis.titan.support.TitanAppException;
import com.spike.giantdataanalysis.titan.support.TitanAppGraphDataOperation;
import com.spike.giantdataanalysis.titan.support.TitanAppGraphSchemaOperation;
import com.spike.giantdataanalysis.titan.support.TitanOps;
import com.spike.giantdataanalysis.titan.support.TitanStrings;
import com.thinkaurelius.titan.core.Cardinality;
import com.thinkaurelius.titan.core.EdgeLabel;
import com.thinkaurelius.titan.core.Multiplicity;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.VertexLabel;
import com.thinkaurelius.titan.core.schema.TitanManagement;

/**
 * 示例: Schema和数据建模.
 * <p>
 * 内容
 * <ul>
 * <li>定义边(Edge)的Label, 重数
 * <li>定义属性键(Property Key), 数据类型和基数
 * <li>关系类型(Relation Type)
 * <li>定义节点(Vertex)的Label
 * <li>自动Schema创建: see DefaultSchemaMaker.
 * <li>修改Schema: 定义不可修改, Schema元素的名称可以修改但不立即可见.
 * </ul>
 * <p>
 * 高级特性: TODO(zhoujiagen) Well, these features may never be used!
 * <ul>
 * <li>静态节点
 * <li>边和节点的TTL
 * <li>多重属性
 * <li>单向边
 * </ul>
 */
public class ExampleSchemaModeling {

  public static void main(String[] args) {

    try {
      TitanGraph graph = TitanOps.newMemoryGraph();

      // define_edge_label(graph);
      // define_property_key(graph);
      // define_relation_type(graph);
      define_vertex_label(graph);

      TitanOps.close(graph);
      TitanOps.clean(graph);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  // 定义边的标签
  // 可以在图事务或管理事务中创建
  // 默认multiplicity为Multiplicity.MULTI
  static void define_edge_label(final TitanGraph graph) {
    new TitanAppGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        EdgeLabel follow = tm.makeEdgeLabel("follow").multiplicity(Multiplicity.MULTI).make();
        System.out.println(TitanStrings.repr(follow));

        // v1 (*) -(mother) -> (1) v2: v1的mother是v2 - MANY2ONE
        EdgeLabel mother = tm.makeEdgeLabel("mother").multiplicity(Multiplicity.MANY2ONE).make();
        System.out.println(TitanStrings.repr(mother));

        // v1 (1) -(winnerOf) -> (*) v2: v1赢了v2 - ONE2MANY
        EdgeLabel winnerOf =
            tm.makeEdgeLabel("winnerOf").multiplicity(Multiplicity.ONE2MANY).make();
        System.out.println(TitanStrings.repr(winnerOf));

        EdgeLabel marriedTo =
            tm.makeEdgeLabel("marriedTo").multiplicity(Multiplicity.ONE2ONE).make();
        System.out.println(TitanStrings.repr(marriedTo));
      }
    }.execute(graph);
  }

  // 定义属性键
  // 默认属性键值的基数为Cardinality.SINGLE
  // 可以在图事务或管理事务中创建
  // Titan支持的数据类型: String, Boolean, Byte, Short, Integer
  // Long, Float, Double, Decimal, Precision, Date, GeoShape, UUID
  static void define_property_key(final TitanGraph graph) {
    new TitanAppGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        PropertyKey birthDate =
            tm.makePropertyKey("birthDate").dataType(Long.class).cardinality(Cardinality.SINGLE)
                .make();
        System.out.println(TitanStrings.repr(birthDate));

        PropertyKey name =
            tm.makePropertyKey("name").dataType(String.class).cardinality(Cardinality.SET).make();
        System.out.println(TitanStrings.repr(name));

        PropertyKey sensorReading =
            tm.makePropertyKey("sensorReading").dataType(Double.class)
                .cardinality(Cardinality.LIST).make();
        System.out.println(TitanStrings.repr(sensorReading));
      }
    }.execute(graph);
  }

  // 定义关系类型: 边标签或者属性键
  // 关系类型的名称在图中必须唯一
  static void define_relation_type(final TitanGraph graph) {
    new TitanAppGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        try {

          String name = "name";

          tm.makePropertyKey(name).dataType(String.class).cardinality(Cardinality.SET).make();

          if (tm.containsRelationType(name)) {
            PropertyKey pk = tm.getPropertyKey(name);
            System.out.println(pk == null ? "NULL" : TitanStrings.repr(pk));

            EdgeLabel el = tm.getEdgeLabel(name);
            System.out.println(el == null ? "NULL" : TitanStrings.repr(el));
          }

        } catch (IllegalArgumentException e) {
          e.printStackTrace();
          throw TitanAppException.newException(e);
        }

      }
    }.execute(graph);
  }

  // 定义节点的标签, 可选.
  static void define_vertex_label(final TitanGraph graph) {
    new TitanAppGraphSchemaOperation() {
      @Override
      public void operation(TitanManagement tm) throws TitanAppException {
        VertexLabel person = tm.makeVertexLabel("person").make();
        System.out.println(TitanStrings.repr(person));
      }
    }.execute(graph);

    new TitanAppGraphDataOperation() {
      @Override
      public void operation(TitanTransaction tx) throws TitanAppException {
        Vertex alice = tx.addVertex("person");
        alice.property("name", "alice");
        System.out.println(TitanStrings.repr(alice));

        Vertex unlabeled = tx.addVertex();
        unlabeled.property("name", "unlabeled");
        System.out.println(TitanStrings.repr(unlabeled));
      }
    }.execute(graph);

  }

}
