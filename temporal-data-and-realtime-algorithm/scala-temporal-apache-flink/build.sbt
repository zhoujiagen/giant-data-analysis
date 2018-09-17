
organization := "com.spike.giantdataanalysis"
name := "scala-temporal-apache-flink"
version := "1.0.0"
scalaVersion := "2.11.12"

lazy val FLINK_VERSION = "1.6.0"

libraryDependencies ++= Seq(
  "org.apache.flink" % "flink-core" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-scala" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-streaming-scala" % FLINK_VERSION withSources()
  , "org.apache.flink" % "flink-java" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-streaming-java" % FLINK_VERSION withSources() // % "provided"
  , "org.apache.flink" %% "flink-clients" % FLINK_VERSION withSources()
  //,"org.apache.flink" %% "flink-streaming-contrib" % FLINK_VERSION withSources() // for mock sink

  , "com.google.guava" % "guava" % "19.0" withSources()
  // ,"org.slf4j" % "slf4j-api" % "1.7.7" // % "provided"
  , "org.slf4j" % "slf4j-log4j12" % "1.7.7" // % "provided"
  , "log4j" % "log4j" % "1.2.17" // % "provided"

  , "org.apache.flink" %% "flink-test-utils" % FLINK_VERSION % Test

  , "com.data-artisans.streamingledger" % "da-streamingledger-sdk" % "1.0.0"
  , "com.data-artisans.streamingledger" % "da-streamingledger-runtime-serial" % "1.0.0"
)
