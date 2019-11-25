# 数据管理基础设施

## 项目

+ [datasets](datasets/README.md)
+ [infrastructure-ansible](infrastructure-ansible/README.md)
+ [infrastructure-apache-cassandra](infrastructure-apache-cassandra/README.md)
+ [infrastructure-apache-curator](infrastructure-apache-curator/README.md)
+ [infrastructure-apache-hadoop](infrastructure-apache-hadoop/README.md)
+ [infrastructure-apache-hadoop](infrastructure-apache-hadoop/documents/faq.md)
+ [infrastructure-apache-hbase](infrastructure-apache-hbase/README.md)
+ [infrastructure-apache-zookeeper](infrastructure-apache-zookeeper/README.md)
+ [infrastructure-container-docker](infrastructure-container-docker/README.md)
+ [infrastructure-etl](infrastructure-etl/README.md)
+ [infrastructure-jetty](infrastructure-jetty/README.md)
+ [infrastructure-netty](infrastructure-netty/README.md)
+ [infrastructure-opentsdb](infrastructure-opentsdb/README.md)
+ [scala-infrastructure-akka](scala-infrastructure-akka/README.md)
+ [scala-infrastructure-apache-spark](scala-infrastructure-apache-spark/README.md)




### infrastructure-commons-agent

Java Agent示例项目, 使用了javassist.

REF: [Guide to Java Instrumentation](https://www.baeldung.com/java-instrumentation).


构建应用:
```
mvn package -Pdev-buildApplication
$ java -classpath target/libs/*:src/main/resources/:target/infrastructure-commons-agent-0.0.1-SNAPSHOT-application.jar com.spike.giantdataanalysis.commons.agent.ExampleAgentLoader ExampleApplication
2019-03-05 13:20:37,477 [INFO] [main] com.spike.giantdataanalysis.commons.agent.ExampleApplication.hello(ExampleApplication.java:13) => hello
```

构建Agent后运行(`-javaagent`):
```
mvn package -Pdev-buildAgent
$ java -javaagent:target/infrastructure-commons-agent-0.0.1-SNAPSHOT-agent.jar -classpath target/libs/*:src/main/resources/:target/infrastructure-commons-agent-0.0.1-SNAPSHOT-application.jar com.spike.giantdataanalysis.commons.agent.ExampleAgentLoader ExampleApplication
2019-03-05 13:23:57,929 [INFO] [main] com.spike.giantdataanalysis.commons.agent.ExampleAgent.premain(ExampleAgent.java:16) => [Agent] in premain
2019-03-05 13:23:57,940 [INFO] [main] com.spike.giantdataanalysis.commons.agent.ExampleApplicationTransformer.transform(ExampleApplicationTransformer.java:44) => [Agent] Transforming class com.spike.giantdataanalysis.commons.agent.ExampleApplication
2019-03-05 13:23:58,086 [INFO] [main] com.spike.giantdataanalysis.commons.agent.ExampleApplication.hello(ExampleApplication.java:13) => hello
2019-03-05 13:24:03,091 [INFO] [main] com.spike.giantdataanalysis.commons.agent.ExampleApplication.hello(ExampleApplication.java:19) => [Application] hello() completed in:5 seconds!
```
