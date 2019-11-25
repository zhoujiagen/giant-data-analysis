
organization := "com.spike.giantdataanalysis"
name := "scala-temporal-apache-flink"
version := "1.0.0"
scalaVersion := "2.12.10"

lazy val FLINK_VERSION = "1.8.2"

libraryDependencies ++= Seq(
  "org.apache.flink" % "flink-core" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-scala" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-streaming-scala" % FLINK_VERSION withSources()
  //, "org.apache.flink" % "flink-java" % FLINK_VERSION withSources()
  //, "org.apache.flink" %% "flink-streaming-java" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-clients" % FLINK_VERSION withSources()
  //,"org.apache.flink" %% "flink-streaming-contrib" % FLINK_VERSION withSources() // for mock sink

  // connector: rabbitmq
  , "org.apache.flink" %% "flink-connector-rabbitmq" % FLINK_VERSION withSources()
  // queryable state
  , "org.apache.flink" %% "flink-queryable-state-client-java" % FLINK_VERSION % "provided" withSources()

  // table and sql
  , "org.apache.flink" % "flink-table" % FLINK_VERSION % "provided" pomOnly()
  , "org.apache.flink" % "flink-table-common" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-table-api-scala-bridge" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-table-planner" % FLINK_VERSION withSources()
  , "org.apache.flink" %% "flink-jdbc" % FLINK_VERSION withSources()
  , "mysql" % "mysql-connector-java" % "5.1.45"

  // lib
  // CEP
  , "org.apache.flink" %% "flink-cep" % FLINK_VERSION withSources()

  // test
  , "org.scalactic" %% "scalactic" % "3.0.5"
  , "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  , "org.apache.flink" %% "flink-test-utils" % FLINK_VERSION % "test"

  // Streaming Ledger
  , "com.data-artisans.streamingledger" % "da-streamingledger-sdk" % "1.0.0" withSources()
  , "com.data-artisans.streamingledger" % "da-streamingledger-runtime-serial" % "1.0.0" withSources()

  // utilities
  , "com.google.guava" % "guava" % "19.0" withSources()
  , "com.google.code.gson" % "gson" % "2.8.5" withSources()
  , "org.slf4j" % "slf4j-api" % "1.7.7"
  , "org.slf4j" % "slf4j-log4j12" % "1.7.7"
  , "log4j" % "log4j" % "1.2.17"
)

// https://stackoverflow.com/questions/5137460/sbt-stop-run-without-exiting
fork in run := true
cancelable in Global := true
