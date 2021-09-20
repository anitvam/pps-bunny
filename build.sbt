import sbt.Keys.libraryDependencies

ThisBuild / version := "0.2.0"
ThisBuild / organization := "it.unibo"
scalaVersion := "2.13.6"

assembly / mainClass := Some("controller.ScalaFXLauncher")

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-Ymacro-annotations")

name := "pps-bunny"

// Add dependency on ScalaFX library
libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx"             % "16.0.0-R24",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.5"
)

// Add JavaFX dependencies
libraryDependencies ++= {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}

// Add ScalaTest dependencies
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

//Add Cats dependencies
libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.2.0"

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true






