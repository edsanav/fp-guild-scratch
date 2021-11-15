package exercises.rosalind

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.effect.{ExitCode, IO}
import cats.implicits._


import scala.annotation.{tailrec, unused}
import scala.util.{Failure, Success, Try}

object fib {

  def run(@unused input:List[String]):IO[ExitCode] = {
    input match  {
      case nS::kS::_ => parse(nS,kS) match {
        case Invalid(th) => IO.println("Error parsing: "+th) *> IO(ExitCode.Error)
        case Valid((n,k)) => IO.println("result is "+compute(n,k).toString) *> IO(ExitCode.Success)
      }
      case _ => IO(ExitCode.Error)
    }
  }

  def parse(nStr:String, kStr:String):Validated[String, (Int,Int)] = {
    (
      getMessage(Try(nStr.toInt)).toValidated,
      getMessage(Try(kStr.toInt)).toValidated
    ).tupled
  }

  def parseToEither(nStr:String, kStr:String):Either[String, (Int,Int)] = {
    (
      getMessage(Try(nStr.toInt)),
      getMessage(Try(kStr.toInt))
      ).tupled
  }

  def getMessage[A](t:Try[A]):Either[String,A] = {
    t match {
      case Failure(exc) => Left(exc.getMessage)
      case Success(value) => Right(value)
    }
  }

  def compute(N:Int, K:Int):Int = {
    @tailrec
    def inner(month:Int, current:Int, nextGen:Int):Int = {
      month match {
        case N => current
        case _ => {inner(month+1, nextGen, current*K+nextGen)}
      }
    }
    inner(1, 1, 1)
  }


}
