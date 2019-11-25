
organization := "com.spike.giantdataanalysis"
name := "scala-infrastructure-apache-spark"
version := "1.0.0"
scalaVersion := "2.12.10"

// update from 1.5.2 to 2.4.0 20190402
lazy val VERSION_SPARK = "2.4.4"

libraryDependencies ++= Seq (
	"org.apache.spark" %% "spark-core" % VERSION_SPARK withSources()
	,"org.apache.spark" %% "spark-streaming" % VERSION_SPARK withSources()

	,"org.apache.spark" %% "spark-sql" % VERSION_SPARK withSources()
	,"org.apache.spark" %% "spark-hive" % VERSION_SPARK withSources()
	,"mysql" % "mysql-connector-java" % "5.1.35"  withSources()

	,"org.apache.spark" %% "spark-graphx" % VERSION_SPARK withSources()
	,"org.apache.spark" %% "spark-mllib" % VERSION_SPARK withSources()
	//,"com.google.guava" % "guava" % "19.0" withSources()
)
