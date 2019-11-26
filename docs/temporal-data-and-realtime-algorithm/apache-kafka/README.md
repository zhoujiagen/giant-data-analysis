# 0 实践版本

	0.9.0.1

# 1 资源

[0.9.0文档](https://kafka.apache.org/090/documentation.html)

安装后目录下的`site-docs`中文档.

# 2 运行实例

> TBD

# 3 安装和配置

## 3.1 启动
### 单节点单broker
#### Zookeeper Server

配置(config/zookeeper.properties)：

	# the directory where the snapshot is stored.
	# 快照存放目录
	#dataDir=/tmp/zookeeper
	dataDir=/Users/zhang/Documents/software/kafka_2.11-0.9.0.1/data/zookeeper

	# the port at which the clients will connect
	# 暴露给客户端连接的端口
	#clientPort=2181
	clientPort=2188

	# disable the per-ip limit on the number of connections since this is a non-production config
	# 客户端连接最大数量，为0表示不做限制
	maxClientCnxns=0

启动：

	$ bin/zookeeper-server-start.sh config/zookeeper.properties

#### Kafka broker

配置(config/server.properties)：

	# The id of the broker. This must be set to a unique integer for each broker.
	# 当前broker的ID，每个broker的ID必须唯一
	broker.id=0

	# The port the socket server listens on
	# Socket服务端端口
	port=9092

	# A comma seperated list of directories under which to store log files
	# 日志文件
	#log.dirs=/tmp/kafka-logs
	log.dirs=/Users/zhang/Documents/software/kafka_2.11-0.9.0.1/data/kafka-logs

	# The default number of log partitions per topic. More partitions allow greater
	# parallelism for consumption, but this will also result in more files across
	# the brokers.
	# 每个主题日志分区的默认数量。
	# 更多的分区可以增加消费的并行度，但同时会导致更多文件通过brokers
	num.partitions=1

	# Zookeeper connection string (see zookeeper docs for details).
	# This is a comma separated host:port pairs, each corresponding to a zk
	# server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002".
	# You can also append an optional chroot string to the urls to specify the
	# root directory for all kafka znodes.
	# ZooKeeper连接字符串
	#zookeeper.connect=localhost:2181
	zookeeper.connect=localhost:2188

启动：

	$ bin/kafka-server-start.sh config/server.properties

## 3.2 创建Kafka主题(topic)

	$ bin/kafka-topics.sh --create --zookeeper localhost:2188 --replication-factor 1 --partitions 1 --topic kafkatopic
	Created topic "kafkatopic".

	# 查看主题
	$ bin/kafka-topics.sh --list --zookeeper localhost:2188
	kafkatopic

##	3.3 生产消息

配置(config/producer.properties)：

	# list of brokers used for bootstrapping knowledge about the rest of the cluster
	# format: host1:port1,host2:port2 ...
	# broker列表，用于建立集群信息
	metadata.broker.list=localhost:9092

	# name of the partitioner class for partitioning events; default partition spreads data randomly
	# 用于分区事件的分区器名称，默认随机分区
	#partitioner.class=

	# specifies whether the messages are sent asynchronously (async) or synchronously (sync)
	# 指定消息是同步还是异步传递
	producer.type=sync

	# specify the compression codec for all data generated: none, gzip, snappy, lz4.
	# the old config values work as well: 0, 1, 2, 3 for none, gzip, snappy, lz4, respectively
	# 指定生成数据的压缩codec
	compression.codec=none

	# message encoder
	# 消息encoder
	serializer.class=kafka.serializer.DefaultEncoder

生产：

	$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic kafkatopic
	# 输入消息内容, Ctrl+C退出

## 3.4 消费消息

配置(config/consumer.properties)：

	# Zookeeper connection string
	# comma separated host:port pairs, each corresponding to a zk
	# server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002"
	# Zookeeper连接字符串
	#zookeeper.connect=127.0.0.1:2181
	zookeeper.connect=127.0.0.1:2188

	# timeout in ms for connecting to zookeeper
	# 连接Zookeeper超时毫秒数
	zookeeper.connection.timeout.ms=6000

	#consumer group id
	# 消费者的组ID
	group.id=test-consumer-group

	#consumer timeout
	# 消费者的超时毫秒数
	consumer.timeout.ms=5000

消费：

	$ bin/kafka-console-consumer.sh --zookeeper localhost:2188 --topic kafkatopic --from-beginning
	Welcome to  Kafka
	This    is  single  broker  cluster
	^CProcessed a total of 2 messages
