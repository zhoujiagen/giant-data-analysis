name := "Spark_SQL_Example"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq (
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.spark" % "spark-sql_2.10" % "1.5.2"
)
