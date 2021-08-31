import sbt._

object Dependencies {
  object Versions {
    val cats          = "2.2.0"
    val catsEffect    = "3.1.1"

    val fs2           = "3.1.0"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.0"

    val scalaCheck    = "1.15.1"
    val scalaTest     = "3.2.9"
    val scalaTestPlus = "3.2.9.0"

  }

  object Libraries {

    val cats        = "org.typelevel"    %% "cats-core"     % Versions.cats
    val catsEffect  = "org.typelevel"    %% "cats-effect"   % Versions.catsEffect
    // available for 2.12, 2.13, 3.0
    val fs2 = "co.fs2" %% "fs2-core" % Versions.fs2

    // optional I/O library
    val fs2IO = "co.fs2" %% "fs2-io" % Versions.fs2

    // Compiler plugins
    val betterMonadicFor = "com.olegpy"    %% "better-monadic-for" % Versions.betterMonadicFor
    val kindProjector    = "org.typelevel" % "kind-projector"      % Versions.kindProjector


    // Test
    val scalaCheck    = "org.scalacheck"    %% "scalacheck"      % Versions.scalaCheck
    val scalaTest     = "org.scalatest"     %% "scalatest"       % Versions.scalaTest
    val scalaTestPlus = "org.scalatestplus" %% "scalacheck-1-15" % Versions.scalaTestPlus

  }


}
