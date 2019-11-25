// REF http://www.scala-sbt.org/0.13/docs/zh-cn/Multi-Project.html

lazy val commonSettings = Seq(
  organization := "com.spike.giantdataanalysis",
  version := "1.0.0",
  scalaVersion := "2.12.10"
)

name := "scala-parent"

lazy val root = (project in file("."))
  //.aggregate(spark_streaming)
  .aggregate(scala_infrastructure_apache_spark)
  .aggregate(scala_infrastructure_apache_flink)
  .settings(commonSettings: _*)

// RootProject REF http://stackoverflow.com/questions/11653435/how-to-reference-external-sbt-project-from-another-sbt-project
// lazy val spark_streaming = //(project in file("../../temporal-data-and-realtime-algorithm/apache-spark-streaming"))
//  RootProject(file("../../temporal-data-and-realtime-algorithm/temporal-apache-spark-streaming"))

lazy val scala_infrastructure_apache_spark =
  RootProject(file("../../data-management-infrastructure/scala-infrastructure-apache-spark"))

lazy val scala_infrastructure_apache_flink =
    RootProject(file("../../temporal-data-and-realtime-algorithm/scala-temporal-apache-flink"))
