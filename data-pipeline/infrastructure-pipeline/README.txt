########################################################################
Motivation
########################################################################

使用数据流水线, 聚合不同存储格式和Schema的数据源, 提取分析任务感兴趣的特征.

########################################################################
Methodology
########################################################################

保留原有项目/新加项目, 晋升各项目中适用的生产代码到该项目中.

########################################################################
Scope
########################################################################

整合和扩展既有的实验性质计算、存储和任务性项目, 包括:

+ infrastructure-apache-hadoop
+ infrastructure-apache-hbase
+ infrastructure-etl
+ infrastructure-opentsdb
+ infrastructure-task
+ scala-infrastructure-apache-spark
+ representation-apache-giraph
+ representation-apache-tinkerpop
+ representation-neo4j
+ representation-titan
+ temporal-apache-kafka
+ temporal-apache-storm
+ temporal-apache-spark-streaming

待添加的:

+ Apache Hive
+ Apache Pig
+ Apache Flink
+ Apache Beam
+ Apache Apex


########################################################################
Reference
########################################################################

[1] Akidau T, Bradshaw R, Chambers C, et al. The dataflow model: a practical 
approach to balancing correctness, latency, and cost in massive-scale, 
unbounded, out-of-order data processing[J]. 
Proceedings of the VLDB Endowment, 2015, 8(12): 1792-1803.

