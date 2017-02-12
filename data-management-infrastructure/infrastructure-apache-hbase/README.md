# TODO

		create package `com.spike.giantdataanalysis.<specific-project>`

# 0 实践版本

	1.2.3

# 1 资源

+ 本地文档: hbase-1.2.3/docs/index.html

# 2 运行实例

# 3 安装和配置

## 3.1 standalone方式运行

conf/hbase-site.xml

	  <property>
	    <name>hbase.rootdir</name>
	    <value>file:///Users/zhang/data/hbase</value>
	  </property>
	  <property>
	    <name>hbase.zookeeper.property.dataDir</name>
	    <value>/Users/zhang/data/zookeeper</value>
	  </property>

启动/停止:
	
	bin/start-hbase.sh
	bin/stop-hbase.sh

脚本连接:

	bin/hbase shell


	