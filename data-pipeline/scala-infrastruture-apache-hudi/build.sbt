organization := "com.spike.giantdataanalysis"
name := "scala-infrastruture-apache-hudi"
version := "1.0.0"
// due to org.apache.hudi:hudi-spark -> com.databricks:spark-avro
// scalaVersion := "2.12.10"
scalaVersion := "2.11.12"

lazy val HUDI_VERSION = "0.5.0-incubating"
lazy val VERSION_SPARK = "2.4.4"

//val excludeJackson211 = ExclusionRule(organization = "com.fasterxml.jackson.module", name = "jackson-module-scala_2.11")

libraryDependencies ++= Seq(
  "org.apache.hudi" % "hudi-common" % HUDI_VERSION withSources()
  , "org.apache.hudi" % "hudi-client" % HUDI_VERSION withSources()
  , "org.apache.hudi" % "hudi-utilities-bundle" % HUDI_VERSION withSources()
  , "org.apache.hudi" % "hudi-hive-bundle" % HUDI_VERSION withSources()

  , "org.apache.hudi" % "hudi-spark-bundle" % HUDI_VERSION withSources()
  //, "org.apache.hudi" % "hudi-spark" % HUDI_VERSION excludeAll (excludeJackson211) withSources()
  //, "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.0"
  , "org.apache.hudi" % "hudi-spark" % HUDI_VERSION withSources()
  , "org.apache.spark" %% "spark-core" % VERSION_SPARK withSources()
  , "org.apache.spark" %% "spark-sql" % VERSION_SPARK withSources()
  , "org.apache.spark" %% "spark-hive" % VERSION_SPARK withSources()



  , "org.apache.hudi" % "hudi-hadoop-mr-bundle" % HUDI_VERSION withSources()
  , "org.apache.hudi" % "hudi-presto-bundle" % HUDI_VERSION withSources()
  , "org.apache.hudi" % "hudi-timeline-server-bundle" % HUDI_VERSION withSources()

)
