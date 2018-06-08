
//---------------------------------------------------------------------------
// Constants
//---------------------------------------------------------------------------

lazy val VERSION_AKKA = "2.5.12"
lazy val VERSION_SCALATEST = "3.0.5"
lazy val VERSION_LOGBACK = "1.2.3"
lazy val VERSION_GDA = "0.0.1-SNAPSHOT"
lazy val VERSION_LEVELDB = "0.7"
lazy val VERSION_LEVELDB_JNI = "1.8"

//---------------------------------------------------------------------------
// Dependencies
//---------------------------------------------------------------------------

lazy val libraryDependenciesAkka = Seq(
  "com.typesafe.akka" %% "akka-actor" % VERSION_AKKA withSources(),
  "com.typesafe.akka" %% "akka-remote" % VERSION_AKKA withSources(),

  "com.typesafe.akka" %% "akka-persistence" % VERSION_AKKA withSources(),
  "org.iq80.leveldb" % "leveldb" % VERSION_LEVELDB withSources(),
  "org.fusesource.leveldbjni" % "leveldbjni-all" % VERSION_LEVELDB_JNI withSources(),

  "com.typesafe.akka" %% "akka-slf4j" % VERSION_AKKA withSources(),
  "ch.qos.logback" % "logback-classic" % VERSION_LOGBACK,

  "com.spike.giantdataanalysis" % "infrastructure-commons" % VERSION_GDA
)


lazy val libraryDependenciesMisc = Seq(
  // boilerpipe: extract body from web page
  "com.syncthemall" % "boilerpipe" % "1.2.2" withSources()
)

lazy val libraryDependenciesTest = Seq(
  "com.typesafe.akka" %% "akka-testkit" % VERSION_AKKA % "test" withSources(),
  "org.scalactic" %% "scalactic" % VERSION_SCALATEST % "test" withSources(),
  "org.scalatest" %% "scalatest" % VERSION_SCALATEST % "test" withSources()
)


//---------------------------------------------------------------------------
// Settings
//---------------------------------------------------------------------------
lazy val commonSettings = Seq(
  organization := "com.spike.giantdataanalysis",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.12",
  libraryDependencies ++= libraryDependenciesAkka,
  resolvers += Resolver.mavenLocal
)


//---------------------------------------------------------------------------
// Projects
//---------------------------------------------------------------------------

lazy val root = (project in file("."))
  .settings(
    name := "scala-infrastructure-akka",
    commonSettings
  )
  .aggregate(akkaCommons, coreMessages, coreClient, coreServer)

lazy val akkaCommons = (project in file("akka-commons"))
  .settings(
    name := "akka-commons",
    commonSettings,
    libraryDependencies ++= libraryDependenciesTest
  )

lazy val coreClient = (project in file("akka-core-client"))
  .dependsOn(akkaCommons, coreMessages)
  .settings(
    name := "akka-core-client",
    commonSettings,
    libraryDependencies ++= libraryDependenciesTest
  )


lazy val coreServer = (project in file("akka-core-server"))
  .dependsOn(akkaCommons, coreMessages)
  .settings(
    name := "akka-core-server",
    commonSettings,
    libraryDependencies ++= libraryDependenciesTest
  )


lazy val coreMessages = (project in file("akka-core-messages"))
  .settings(
    name := "akka-core-messages",
    commonSettings
  )
