import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "FP Guild Scratch",
    trapExit := false, // Avoid exceptions on non 0 exit code
    scalacOptions += "-Ymacro-annotations",
    assembly / assemblyJarName := "fpguild.jar",
    libraryDependencies ++= Seq(
      compilerPlugin(Libraries.kindProjector cross CrossVersion.full),
      compilerPlugin(Libraries.betterMonadicFor),
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.scalaCheck,
      Libraries.scalaTest,
      Libraries.scalaTestPlus,
      Libraries.fs2,
      Libraries.fs2IO
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
