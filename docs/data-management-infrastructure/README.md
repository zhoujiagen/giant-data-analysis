# 数据管理基础设施

## Containerization

a few clues:

- [big-data-europe/docker-hadoop](https://github.com/big-data-europe/docker-hadoop): Apache Hadoop docker image

## 项目

+ [datasets](datasets/README.md)
+ [ansible](ansible/README.md)
+ [apache-cassandra](apache-cassandra/README.md)
+ [apache-curator](apache-curator/README.md)
+ [apache-hadoop](apache-hadoop/README.md)
+ [apache-hadoop FAQ](apache-hadoop/hadoop-faq.md)
+ [apache-hbase](apache-hbase/README.md)
+ [apache-zookeeper](apache-zookeeper/README.md)
+ [docker](container/docker.md)
+ [K8S](container/k8s.md)
+ [etl](etl/README.md)
+ [jetty](jetty/README.md)
+ [netty](netty/README.md)
+ [opentsdb](opentsdb/README.md)
+ [akka](akka/README.md)
+ [apache-spark](apache-spark/README.md)

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
