name := "play2_6"

version := "1.0"

scalaVersion := "2.12.6"

scalafmtOnCompile in ThisBuild := true

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.9.1"
  )
