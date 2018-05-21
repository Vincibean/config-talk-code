name := "play2_3"

version := "1.0"

scalaVersion := "2.11.12"

scalacOptions += "-Ypartial-unification"

scalafmtOnCompile in ThisBuild := true

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "1.1.0",
      "org.pac4j" % "play-pac4j_scala2.11" % "1.4.0",
      "org.pac4j" % "pac4j-saml" % "1.7.0"
    )
  )
