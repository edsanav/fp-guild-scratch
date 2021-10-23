package exercises.rosalind

import cats.effect.{ExitCode, IO}
import cats.implicits._

import scala.annotation.{tailrec, unused}
import scala.util.Try

object fib {

  def run(@unused input:List[String]):IO[ExitCode] = {
    input match  {
      case nS::kS::_ =>
        IO.fromEither(parse(nS,kS)
          .map{case (n,k) => compute(n,k)})
          .map(println(_)) *> IO(ExitCode.Success)
      case _ => IO(ExitCode.Error)
    }
  }

  def parse(nStr:String, kStr:String):Either[Throwable, (Int,Int)] = {
    (
      Try(nStr.toInt),
      Try(kStr.toInt)
    ).mapN((x,y) => (x,y)).toEither
  }

  def compute(N:Int, K:Int):Int = {
    @tailrec
    def inner(month:Int, current:Int, nextGen:Int):Int = month match {
      case N => current
      case _ => {inner(month+1, nextGen, current*K+nextGen)}
    }
    inner(1, 1, 1)
  }

}
