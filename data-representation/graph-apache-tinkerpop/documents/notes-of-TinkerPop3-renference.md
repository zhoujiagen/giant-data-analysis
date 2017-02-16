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

> TBD The Graph Process

# 2 图(graph)
# 3 遍历(traversal)
# 4 图计算机(`GraphComputer`)
# 5 Gremlin应用
# 6 实现: TinkerGraph-Gremlin, Neo4j-Gremlin, Hadoop-Gremlin.

