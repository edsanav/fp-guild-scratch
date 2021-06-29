import sbt._

object Dependencies {
  object Versions {
    val cats          = "2.2.0"
    val catsEffect    = "3.1.1"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.0"

    val scalaCheck    = "1.15.1"
    val scalaTest     = "3.2.3"
  }

  object Libraries {

    val cats        = "org.typelevel"    %% "cats-core"     % Versions.cats
    val catsEffect  = "org.typelevel"    %% "cats-effect"   % Versions.catsEffect



    // Compiler plugins
    val betterMonadicFor = "com.olegpy"    %% "better-monadic-for" % Versions.betterMonadicFor
    val kindProjector    = "org.typelevel" % "kind-projector"      % Versions.kindProjector


    // Test
    val scalaCheck    = "org.scalacheck"    %% "scalacheck"      % Versions.scalaCheck
    val scalaTest     = "org.scalatest"     %% "scalatest"       % Versions.scalaTest
  }


}
