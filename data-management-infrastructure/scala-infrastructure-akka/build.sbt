//---------------------------------------------------------------------------
// Constants
//---------------------------------------------------------------------------

lazy val VERSION_AKKA = "2.5.12"
lazy val VERSION_SCALATEST = "3.0.5"


//---------------------------------------------------------------------------
// Dependencies
//---------------------------------------------------------------------------

lazy val libraryDependenciesAkka = Seq(
  "com.typesafe.akka" %% "akka-actor" % VERSION_AKKA withSources(),
  "com.typesafe.akka" %% "akka-remote" % VERSION_AKKA withSources()
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
  libraryDependencies ++= libraryDependenciesAkka
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
