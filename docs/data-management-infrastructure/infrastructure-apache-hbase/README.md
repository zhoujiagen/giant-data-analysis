
# 0 实践版本

	1.2.3

# 1 资源

+ 本地文档: hbase-1.2.3/docs/index.html

+ 异步客户端

[asynchbase](https://github.com/OpenTSDB/asynchbase)
A fully asynchronous, non-blocking, thread-safe, high-performance HBase client.

[async](https://github.com/OpenTSDB/async)
Building blocks for asynchronous Java processing inspired by Twisted's API. 


# 2 运行实例

# 3 安装和配置

## 3.1 standalone方式运行

conf/hbase-site.xml

	  <property>
	    <name>hbase.rootdir</name>
	    <value>file:///Users/jiedong/data/hbase</value>
	  </property>
	  <property>
	    <name>hbase.zookeeper.property.dataDir</name>
	    <value>file:///Users/jiedong/data/zookeeper</value>
	  </property>

启动/停止:
	
	bin/start-hbase.sh
	bin/stop-hbase.sh

脚本连接:

	bin/hbase shell

# 4 数据存储模式

	(Table, RowKey, Family, Column, Timestamp) -> Value
	
	SortedMap<RowKey, List<SortedMap<Column, List<Value, Timestamp>>>>
	
	10^9行 * 10^6列 * 10^3版本 = TB/PB级存储 




	