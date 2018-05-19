
organization := "com.spike.giantdataanalysis"
name := "scala-temporal-apache-flink"
version := "1.0.0"
scalaVersion := "2.11.8"


libraryDependencies ++= Seq (
	"org.apache.flink" % "flink-core" % "1.4.2" withSources()
	,"org.apache.flink" %% "flink-scala" % "1.4.2" withSources()
	,"org.apache.flink" %% "flink-streaming-scala" % "1.4.2" withSources()
	,"org.apache.flink" %% "flink-streaming-java" % "1.4.2" withSources() // % "provided"
	,"org.apache.flink" %% "flink-clients" % "1.4.2" withSources()
	,"org.apache.flink" %% "flink-streaming-contrib" % "1.4.2" withSources() // for mock sink
	
	, "com.google.guava" % "guava" % "19.0" withSources()
	// ,"org.slf4j" % "slf4j-api" % "1.7.7" // % "provided"
	,"org.slf4j" % "slf4j-log4j12" % "1.7.7" // % "provided"
	,"log4j" % "log4j" % "1.2.17" // % "provided"
	
	//,"org.apache.flink" %% "flink-test-utils" % "1.4.2" % Test

)
