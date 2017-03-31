
organization := "com.spike.giantdataanalysis"
name := "scala-infrastructure-apache-spark"
version := "1.0.0"
scalaVersion := "2.11.8"


libraryDependencies ++= Seq (
	"org.apache.spark" %% "spark-core" % "1.5.2" withSources()
	,"org.apache.spark" %% "spark-streaming" % "1.5.2" withSources()
	
	,"org.apache.spark" %% "spark-sql" % "1.5.2" withSources()
	,"org.apache.spark" %% "spark-hive" % "1.5.2" withSources()
	,"mysql" % "mysql-connector-java" % "5.1.35"  withSources()
	
	,"org.apache.spark" %% "spark-graphx" % "1.5.2" withSources()
	,"org.apache.spark" %% "spark-mllib" % "1.5.2" withSources()
	,"com.google.guava" % "guava" % "19.0" withSources()
	
)