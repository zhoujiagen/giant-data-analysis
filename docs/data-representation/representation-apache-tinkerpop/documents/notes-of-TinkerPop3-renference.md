文档路径: `docs/reference/index.html`.

内容:

+ 介绍图计算
+ 图(graph)
+ 遍历(traversal)
+ 图计算机(`GraphComputer`)
+ Gremlin应用
+ 实现: TinkerGraph-Gremlin, Neo4j-Gremlin, Hadoop-Gremlin.
+ Gremlin变体: Gremlin-Python (NOT)

# 1 介绍图计算

图计算显式区分了结构(graph)和处理(traversal).

TinkerPop3结构API的主要元素:

+ `Graph` 
+ `Element`: `Vertex`, `Edge`
+ `Property<V>`(键为字段串, 值为`V`): `VertexProperty<V>`

TinkerPop3处理API的主要元素:

+ `TraversalSource`: 图/DSL/执行引擎遍历生成器

`Traversal<S,E>`: 函数式数据流处理, 将类型`S`的对象转化为类型`T`;

`GraphTraversal`: 面向原始图(节点, 边等)的遍历DSL.

+ `GraphComputer`: 并行及分布式处理图的系统

`VertexProgram`: 代码在节点上以逻辑并行的方式执行, 通过传递消息通信;

`MapReduce`: 并行的分析图中所有节点的计算, 生成单一的规约后的结果.


Gremlin中的遍历者`GraphTraversal`由`TraversalSource`生成.

`GraphTraversal`支持函数聚合, 其每个方法称为步骤(step), 每个步骤以下述方式处理前一步骤的结果(即通用的步骤):

+ map: S -> E; `MapStep`
+ flatMap: S -> E\*, 其中E\*表示E上的迭代器; `FlatMapStep`
+ filter: S -> S', S <= S; `FilterMap`
+ sideEffect: 允许遍历者不变, 但额外产生计算副作用; `SideEffectStep`
+ branch: S -> {S1 -> E\*, S2 -> E\*, ...} -> E\*; `BranchStep`.

遍历执行时, 遍历源在表达式的左端, 步骤在表达式中部, 结果由表达式右边的`traversal.next()`生成.

在TinkerPop3中, 在遍历中传播的对象被封装为`Traverser<T>`.

# 2 图(graph)

`Feature`实现描述的是`Graph`的能力, 包括图/变量/节点/节点属性/边/边属性特性.

`VertexProperty`支持多同名属性和原属性(即属性上的属性).

图变量`Graph.Variables`: 图上的属性.

图事务`Transaction`(`Graph.tx()`获取), 支持线程内事务(`ThreadLocal`中)和跨线程事务(`Transaction.createThreadedTx()`).

Gremlin I/O负责图数据的存储, 格式有XML, JSON, Kryo. 


# 3 遍历(traversal)

遍历抽象`Traversal<S,E>`, 实现了`Iterator<E>`, `S`(start), `E`(end); 由四个主要元素构成:

+ `Step<S, E>`: 应用到`S`产生`E`的独立函数, 在遍历中构成链;
+ `TraversalStrategy`: 修改遍历执行的拦截器方法, 例如查询重写;
+ `TraversalSideEffects`: 可用于存储遍历全局信息的键值对;
+ `Traverser<T>`: 表示当前在遍历中传播的类型`T`的对象.

具体的实现: `GraphTraversal<S,E>`. 可以从`GraphTraversalSource`或者`org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__`中生成.

图遍历由步骤的顺序列表构成.

## General Steps

5种通用的步骤:  map, flatMap, filter, sideEffect, branch.

遍历者`Traverser<S>`提供了访问接口:

+ 当前被遍历对象`S`(`Traverser.get()`)
+ 遍历者遍历的当前路径(`Traverser.path()`)
+ 在当前循环中遍历者已走的次数(`Traverser.loop()`)
+ 遍历者表示的对象数量(`Traverser.bulk()`)
+ 遍历者关联的局部数据结构(`Traverser.sack()`)
+ 遍历者关联的副作用(`Traverser.sideEffects()`)

详情见`com.spike.giantdataanalysis.tinkerpop.example.TinkerPopTraversalStepExample`.

> TBD

# 4 图计算机(`GraphComputer`)
# 5 Gremlin应用
# 6 实现: TinkerGraph-Gremlin, Neo4j-Gremlin, Hadoop-Gremlin.

