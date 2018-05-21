resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.13")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")

logLevel := Level.Warn