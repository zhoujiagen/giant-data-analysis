# TODO

READING LIST:

+ [Getting Started with Graph Databases](https://academy.datastax.com/resources/getting-started-graph-databases)
+ [Getting Started](http://s3.thinkaurelius.com/docs/titan/1.0.0/getting-started.html)
+ [Titan  0.9.0-M1 with Scala](https://jaceklaskowski.gitbooks.io/titan-scala/content/): seems no pure code.

# 0 实践版本

	1.0.0

# 1 资源

+ [Titan 1.0.0文档](http://s3.thinkaurelius.com/docs/titan/1.0.0/index.html)
+ [Titan存储索引后端的兼容性](http://s3.thinkaurelius.com/docs/titan/1.0.0/version-compat.html)

Titan 			1.0.0(hadoop1)
Faunus 			-
Cassandra 		1.2.z, 2.0.z, 2.1.z
HBase 			0.94.z, 0.96.z, 0.98.z, 1.0.z(1.0.3)
Elasticsearch 	1.5.z(1.5.2)
Solr 			5.2.z
TinkerPop		3.0.z

+ [Titan with HBase](http://s3.thinkaurelius.com/docs/titan/1.0.0/hbase.html)
+ [Titan with ES](http://s3.thinkaurelius.com/docs/titan/1.0.0/elasticsearch.html)
+ [Titan配置参考](http://s3.thinkaurelius.com/docs/titan/1.0.0/titan-config-ref.html)

+ [Titan Schema and Data Modeling](http://s3.thinkaurelius.com/docs/titan/1.0.0/schema.html)
+ [Titan Advanced Schema](http://s3.thinkaurelius.com/docs/titan/1.0.0/advanced-schema.html)

# 2 运行实例

# 3 安装和配置

	# start Gremlin Server with Cassandra/ES forked into a separate process
	# 注意: 使用Java 8
	bin/titan.sh start
	
	# Connecting to Gremlin Server
	bin/gremlin.sh
	gremlin> :remote connect tinkerpop.server conf/remote.yaml

	# 加载示例图
	gremlin> graph = TitanFactory.open('conf/titan-cassandra-es.properties')
	gremlin> GraphOfTheGodsFactory.load(graph)
	gremlin> g = graph.traversal()



