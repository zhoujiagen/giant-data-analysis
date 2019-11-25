# 项目介绍

围绕[美国国家学术院国家研究委员会编著的《海量数据分析前沿》](https://www.amazon.cn/gp/product/B00X52U9P6/ref=oh_aui_detailpage_o09_s00?ie=UTF8&psc=1)的实践.


## 相关项目

[SpringDataBasedNoSQLTutorials](https://github.com/zhoujiagen/SpringDataBasedNoSQLTutorials): Spring数据操作.

[Text](https://github.com/zhoujiagen/Text): 文本数据操作.

[Triple](https://github.com/zhoujiagen/Triple): Hadoop ecosystem. - removed

[Parallel-Programming-Examples](https://github.com/zhoujiagen/Parallel-Programming-Examples): 并行计算. - removed

[Jena-Based-Semantic-Web-Tutorial](https://github.com/zhoujiagen/Jena-Based-Semantic-Web-Tutorial): 基于Jena的语义网.

## 数据管道

[data-pipeline](data-pipeline/README.md)


+ infrastructure-pipeline - Data Transform Pipeline ahead of Data Analysis
+ infrastructure-benchmark - Performance Beanchmark
+ infrastructure-communication - Communication practices
+ infrastructure-coordination - Distributed Coordination: leader election, grouping, lock, etc
+ infrastructure-task - Task Partition and Execution


## 0 Commons

[project-commons](project-commons/README.md)




## 1 数据管理基础设施

[data-management-infrastructure](data-management-infrastructure/README.md)


+ datasets - 数据集信息
+ Apache Hadoop 2.7.4/1.2.1 - infrastructure-apache-hadoop
+ Apache Spark 1.5.2 - scala-infrastructure-apache-spark
+ Netty 4.1.8.Final - infrastructure-netty
+ Jetty 9.4.3.v20170317 - infrastructure-jetty
+ Apache ZooKeeper 3.4.6 - infrastructure-apache-zookeeper
+ Apache Curator 4.0.0 - infrastructure-apache-curator
+ Apache HBase 1.2.6 - infrastructure-apache-hbase
+ Ansible 2.3.0.0 - infrastructure-ansible
+ OpenTSDB 2.3.0 - infrastructure-opentsdb
+ Apache Cassandra 3.11.2 - infrastructure-apache-cassandra

辅助项目:

+ infrastructure-commons - 公共依赖项目
+ infrastructure-commons-agent - Java Agent示例项目
+ infrastructure-test - 测试
+ infrastructure-etl - Data ETL
+ infrastructure-python2 - Python2 facilities
+ infrastructure-python3 - Python3 facilities


## 2 时序数据和实时算法

[temporal-data-and-realtime-algorithm](temporal-data-and-realtime-algorithm/README.md)

+ Apache Kafka 0.9.0.1 - temporal-apache-kafka
+ Apache Storm 0.9.7 - temporal-apache-storm
+ Apache Spark Streaming 1.5.2 - temporal-apache-spark-streaming


## 3 数据表示

[data-representation](data-representation/README.md)


+ graph-apache-tinkerpop - representation-apache-tinkerpop 图计算框架; 图遍历语言Gremlin抽象
+ Neo4j 3.2.2 - representation-neo4j 图数据库
+ Apache Jena 3.4.0 - representation-apache-jena
+ Apache Giraph 1.2.1 - representation-apache-giraph 基于BSP的图处理框架
+ Titan 1.0.0 - representation-titan 分布式图数据库(HBase 1.0.3/0.98.2-hadoop1, ES 1.5.2)
+ Elasticsearch 6.2.4 - representation-text-elasticsearch ES


## 4 数据模型

[data-models](data-models/README.md)


+ 图模型 - datamodel-graph


## 5 数据采样


[data-sampling](data-sampling/README.md)


## 6 数据交互

[data-interaction](data-interaction/README.md)

## 7 数据计算巨人

[data-computing-giants](data-computing-giants/README.md)


## 8 patches


+ [patches-opentsdb](patches/opentsdb.md)
