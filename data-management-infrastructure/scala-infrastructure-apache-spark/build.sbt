
organization := "com.spike.giantdataanalysis"
name := "scala-infrastructure-apache-spark"
version := "1.0.0"
scalaVersion := "2.11.8"


libraryDependencies ++= Seq (
	"org.apache.spark" %% "spark-core" % "1.5.2"
	,"org.apache.spark" %% "spark-streaming" % "1.5.2"
	,"org.apache.spark" %% "spark-sql" % "1.5.2"
	,"org.apache.spark" %% "spark-graphx" % "1.5.2"
	,"org.apache.spark" %% "spark-mllib" % "1.5.2"
)