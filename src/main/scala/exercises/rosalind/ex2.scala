package exercises.rosalind

import cats.effect.{ExitCode, IO, Resource}


import scala.io.Source

// Naive implementation (does not process errors)
object ex2 {
  def run(args:List[String]):IO[ExitCode] = {
    args match {
      case input::_ => loadInput(input).use{rawInput=>
       IO.println {
         val result = parseSequences(rawInput).view.mapValues(computeGC).toMap.maxBy(_._2)
         s"Result is ${result}"
       } *> IO(ExitCode.Success)
      }
      case _ => IO.println(s"Errors while executing exercise: Invalid input") *> IO(ExitCode.Error)
    }
  }

  def loadInput(path: String): Resource[IO, String] =
    Resource.make {
      IO(Source.fromResource(path))
    } { source =>
      IO(source.close()).handleErrorWith(_ => IO.unit)
    }.map(source => source.mkString)

  def parseSequences(s:String): Map[String, String] = {
    s
      .split('>')
      .filter(_.nonEmpty)
      .map(_.split("\n", 2))
      .map( arr => (arr(0), arr(1).replace("\n","")))
      .toMap
  }

  def computeGC(s:String):Double = {
    if (s.isEmpty) 0.0
    else {
      val byNT = s.groupMapReduce(k => k)(_ => 1)(_ + _)
      (byNT.getOrElse('G', 0) + byNT.getOrElse('C', 0)) / s.length.toDouble
    }
  }


}
