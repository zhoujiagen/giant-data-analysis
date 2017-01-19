// REF http://www.scala-sbt.org/0.13/docs/zh-cn/Multi-Project.html

lazy val commonSettings = Seq(
  organization := "com.spike.giantdataanalysis",
  version := "1.0.0",
  scalaVersion := "2.11.8"
)

name := "scala-parent"

lazy val root = (project in file("."))
  //.aggregate(spark_streaming)
  .aggregate(scala_infrastructure_apache_spark)
  .settings(commonSettings: _*)

// RootProject REF http://stackoverflow.com/questions/11653435/how-to-reference-external-sbt-project-from-another-sbt-project
// lazy val spark_streaming = //(project in file("../../temporal-data-and-realtime-algorithm/apache-spark-streaming"))
//  RootProject(file("../../temporal-data-and-realtime-algorithm/apache-spark-streaming"))

lazy val scala_infrastructure_apache_spark =
  RootProject(file("../../data-management-infrastructure/scala-infrastructure-apache-spark"))


/**
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq (
      "org.apache.spark" %% "spark-core" % "1.5.2"
      ,"org.apache.spark" %% "spark-streaming" % "1.5.2"
      //,"org.apache.spark" %% "spark-sql" % "1.5.2"
      //,"org.apache.spark" %% "spark-graphx" % "1.5.2"
      //,"org.apache.spark" %% "spark-mllib" % "1.5.2"
    )
  )
*/
