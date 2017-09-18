# 项目介绍

围绕[美国国家学术院国家研究委员会编著的《海量数据分析前沿》](https://www.amazon.cn/gp/product/B00X52U9P6/ref=oh_aui_detailpage_o09_s00?ie=UTF8&psc=1)的实践.


# 相关项目

[SpringDataBasedNoSQLTutorials](https://github.com/zhoujiagen/SpringDataBasedNoSQLTutorials): Spring数据操作.


[Text](https://github.com/zhoujiagen/Text): 文本数据操作.


[Triple](https://github.com/zhoujiagen/Triple): Hadoop ecosystem. - removed


[Parallel-Programming-Examples](https://github.com/zhoujiagen/Parallel-Programming-Examples): 并行计算. - removed


[Jena-Based-Semantic-Web-Tutorial](https://github.com/zhoujiagen/Jena-Based-Semantic-Web-Tutorial): 基于Jena的语义网.


# 内容


## 1 数据管理基础设施 - data-management-infrastructure

+ datasets

+ Apache Hadoop 2.7.4/1.2.1 - infrastructure-apache-hadoop
+ Apache Spark 1.5.2 - scala-infrastructure-apache-spark
+ Netty 4.1.8.Final - infrastructure-netty
+ Jetty 9.4.3.v20170317 - infrastructure-jetty
+ Apache ZooKeeper 3.4.6 - infrastructure-apache-zookeeper
+ Apache Curator 2.12.0 - infrastructure-apache-curator
+ Apache HBase 1.2.6 - infrastructure-apache-hbase
+ Ansible 2.3.0.0 - infrastructure-ansible

+ DOING: Ansible
+ TODO 收集和整理Hadoop2运行示例和配置; Hadoop MapReduce抽象矩阵乘法和关系代数运算.
+ TODO Apache Jena Elephant子项目的示例; 可能的尽快实现类SPARQL端点的功能.

> TBD

## 2 时序数据和实时算法 - temporal-data-and-realtime-algorithm

+ Apache Kafka 0.9.0.0 - temporal-apache-kafka
+ Apache Storm 0.9.7 - temporal-apache-storm
+ Alibaba JStorm - temporal-alibaba-jstorm
+ Apache Spark Streaming 1.5.2 - temporal-apache-spark-streaming


Strike 1: Apache Storm word count example.

+ TODO Strike 2: Spark-Streaming + ElasticSearch + Kafka 日志应用

> TBD

## 3 数据表示 - data-representation

+ graph-apache-tinkerpop - representation-apache-tinkerpop 图计算框架; 图遍历语言Gremlin抽象
+ graph-titan 1.0.0 - representation-titan 分布式图数据库
+ Neo4j 3.2.2 - representation-neo4j 图数据库
+ Apache Jena 3.4.0 - representation-apache-jena
+ Apache Giraph 1.2.1 - representation-apache-giraph 基于BSP的图处理框架

+ TODO 继续整理Step示例; 完成NoSQL Distilled中关系图信息查询.
+ TinkerPop栈, Neo4j遍历API, 准备应用: 描述逻辑的DSL.
+ Neo4j已完成基本探索, 准备实现描述逻辑的基础构造. 20170813 move `com.spike.giantdataanalysis.neo4j.dsl` to `data-models/datamodel-graph`

> TBD

## 4 数据模型 - data-models

+ 图模型 - datamodel-graph

> TBD

## 5 数据采样 - data-sampling

> TBD

## 6 数据交互 - data-interaction

> TBD

## 7 数据计算巨人 - data-computing-giants

> TBD
