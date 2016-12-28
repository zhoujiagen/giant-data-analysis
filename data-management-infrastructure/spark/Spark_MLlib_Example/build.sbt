// 日期：20160531
// netlib的引入见http://spark.apache.org/docs/1.5.2/mllib-guide.html

name := "Spark_MLlib_Example"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq (
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.spark" % "spark-mllib_2.10" % "1.5.2",
  "com.github.fommil.netlib" % "all" % "1.1.2"
)
