
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


	//,"org.apache.flink" %% "flink-test-utils" % "1.4.2" % Test

)
