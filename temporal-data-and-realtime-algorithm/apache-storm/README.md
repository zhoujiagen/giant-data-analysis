# TODO

Trident相关类的层次接口图, 见`documents/types.gliffy`.

# 0 实践版本

	0.9.7
	
# 1 资源

+ Goetz P. T., O'Neill B.. Storm分布式实时计算模式(Storm Blueprints: Patterns for Distributed Real-time Computation). 机械工业出版社. 2015.

+ [0.9.7文档](http://storm.apache.org/releases/0.9.7/index.html)

+ [Storm Kafka Integration](http://storm.apache.org/releases/0.9.7/storm-kafka.html)

# 2 运行实例

见`com.spike.giantdataanalysis.storm.wordcount`.


+ 注意事项

`OpaqueTridentKafkaSpout`在`LocalCluster`部署模式中运行时, 需要引入kafka依赖:

		<dependency>
			<groupId>org.apache.kafka</groupId>
			<artifactId>kafka_2.11</artifactId>
			<version>${kafka.version}</version>
		</dependency>


# 3 安装和配置

见`documents/conf`和`documents/scripts`.

