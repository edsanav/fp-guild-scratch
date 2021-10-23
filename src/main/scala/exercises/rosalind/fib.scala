package exercises.rosalind

import cats.effect.{ExitCode, IO}
import cats.implicits._

import scala.annotation.{tailrec, unused}
import scala.util.Try

object fib {

  def run(@unused input:List[String]):IO[ExitCode] = {
    IO.println(computeDead(6,3)) *> IO(ExitCode.Success)
//    input match  {
//      case nS::kS::_ =>
//        IO.fromEither(parse(nS,kS)
//          .map{case (n,k) => compute(n,k)})
//          .map(println(_)) *> IO(ExitCode.Success)
//      case _ => IO(ExitCode.Error)
//    }
  }

  def parse(nStr:String, kStr:String):Either[Throwable, (Int,Int)] = {
    (
      Try(nStr.toInt),
      Try(kStr.toInt)
    ).mapN((x,y) => (x,y)).toEither
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

  def compute2(N:Int, K:Int):Int = {
    @tailrec
    def inner(month:Int, adults:Int, children:Int):Int = {
      month match {
        case N => adults+children
        case _ => inner(month+1, adults+children, adults*K)
      }
    }
    inner(1, 0, 1)
  }

  def compute3(N:Int, K:Int):Int = {
    def inner(month:Int, adults:Int, children:Int):Int = {
      month match {
        case N => adults+children
        case _ => adults + inner(month+1, children, adults*K)
      }
    }
    inner(1, 0, 1)
  }

  def computeDead(N:Int, M:Int, K:Int=0):Int = {

    def inner(totalMonth:Int, remainingMonths:Int, adults:Int, children:Int):Int = {
      (totalMonth, remainingMonths) match {
        case (_, 0) => 0
        case (N, _) => adults+children
        case _ => inner(totalMonth+1, remainingMonths-1, adults, children) + inner(totalMonth+1, remainingMonths-1, children, 0) + inner(totalMonth+1, remainingMonths-1, children, adults*K)
      }
    }
    inner(1, M, 0, 1)
  }

  /*
  (1(3), 1(2)
  (1, 1)
  (1, 2)
  (2, 3)
  (3, 5)
  (5, 8)

  1,             1(2)
  1(1),          1(0)+1(2))
  1(0)+1(2),    1(0)+1(1)+1(2)
  1(1)+1(2),

   */

}
