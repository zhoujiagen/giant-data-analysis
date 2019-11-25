

# 0 实践版本

	1.4.2
	1.8.2 // 20191125

# 1 资源

+ [Downloads](http://flink.apache.org/downloads.html): tar and Maven dependency.
+ [Introduction to Apache Flink®](http://flink.apache.org/introduction.html)



# 2 运行实例


## [Quickstart](https://ci.apache.org/projects/flink/flink-docs-release-1.4/quickstart/setup_quickstart.html)


		import org.apache.flink.streaming.api.scala._

		NOT!!!:
		implicit val typeInfo = TypeInformation.of(classOf[WordWithCount])
    implicit val typeInfoString = TypeInformation.of(classOf[String])


Steps


		bin/start-local.sh
		tail log/flink-*-jobmanager-*.log
		>>> http://localhost:8081/

		>>> sbt> publishLocal

		nc -l 9000
		bin/flink run /Users/zhang/.ivy2/local/com.spike.giantdataanalysis/scala-temporal-apache-flink_2.11/1.0.0/jars/scala-temporal-apache-flink_2.11.jar

		>>> input something:
		lorem ipsum
		ipsum ipsum ipsum
		bye

		tail -f log/flink-*-taskmanager-*.out

		bin/stop-local.sh



# 3 安装和配置
