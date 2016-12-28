#############################################################################
### 文档说明
### 
### Spark程序示例
#############################################################################

#############################################################################
### 0 项目结构

以Spark_Stream_Example项目为例：

├── build.sbt						# 指定依赖
├── data							# 数据
│   └── people.json
├── project
│   └── plugins.sbt					# 指定sbt插件
├── run.sh							# 运行脚本
└── src
    ├── main
    │   ├── java					# Java代码
    │   │   └── com
    │   │       └── spike
    │   │           └── nio2
    │   │               └── socket
    │   │                   └── BlockingTCPServer.java
    │   ├── scala					# Scala代码
    │   │   └── PrintError.scala
    │   └── scala-2.10
    └── test
        ├── java
        ├── scala
        └── scala-2.10

15 directories, 6 files

=============================================================================
=== build.sbt
name := "Spark_Stream_Example"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq (
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.spark" % "spark-streaming_2.10" % "1.5.2"
)

=============================================================================
=== project/plugins.sbt
// eclispe插件，参考链接：[scala eclipse sbt 应用程序开发](http://blog.csdn.net/oopsoom/article/details/38363369)
// sbt后执行eclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

=============================================================================
=== run.sh

### 1 package jar
#sbt clean package


### 2 submit spark tasks
#...
#[info] Packaging {..}/{..}/target/scala-2.10/simple-project_2.10-1.0.jar
#
## Use spark-submit to run your application
#$ YOUR_SPARK_HOME/bin/spark-submit \
#  --class "SimpleApp" \
#  --master local[4] \
#target/scala-2.10/simple-project_2.10-1.0.jar
#...
#Lines with a: 46, Lines with b: 23
/home/zhoujiagen/devtools/spark-1.5.2-bin-hadoop2.6/bin/spark-submit \
  --class "PrintError" \
  target/scala-2.10/spark_stream_example_2.10-1.0.jar

#############################################################################
### 1 项目概述

=============================================================================
=== 1.1 Console Log Mining
日志数据挖掘：筛选过滤

=============================================================================
=== 1.2 Word Count Example
单词计数

=============================================================================
=== 1.3 Spark_SQL_Example
Spark SQL示例

=============================================================================
=== 1.4 Spark_Stream_Example
Spark Stream示例

