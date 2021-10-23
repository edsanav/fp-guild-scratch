package exercises.rosalind

import cats.effect.{ExitCode, IO}
import cats.implicits._

import scala.util.Try

object revc {


  def run(input:String):IO[ExitCode] = {
    input.split(' ').toList match  {
      case n::k::_ =>  ???
      case _ => IO(ExitCode.Error)
    }
  }

  def parse(nStr:String, kStr:String):Either[Throwable, Int] = {
    (
      Try(nStr.toInt),
      Try(kStr.toInt)
    ).mapN((n,k) => compute(n,k)).toEither
  }

  def compute(n:Int, k:Int):Int = ???

}
