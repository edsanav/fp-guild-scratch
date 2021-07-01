package exercises

import cats.Show
import cats.effect.{ExitCode, IO}
import cats.implicits._

object rosalind {


  def run(args:List[String]):IO[ExitCode] = {
    val result = args match {
      case "ex1"::input::_ => exercise1(input).map(_.show)
      case _ => Left("Invalid input")
    }
    result match  {
      case Right(out) => IO.println(out) *> IO(ExitCode.Success)
      case Left(err) => IO.println(s"Errors while executing exercise: $err") *> IO(ExitCode.Error)

    }
  }

  case class Composition(A:Int, C:Int, G:Int, T:Int) {
    def add(nuc:Char):Either[String, Composition] = nuc match {
      case 'A' => Right(Composition(A+1, C, G, T))
      case 'C'=>Right(Composition(A, C+1, G, T))
      case 'G' => Right(Composition(A, C, G+1, T))
      case 'T'=> Right(Composition(A, C, G, T+1))
      case _ => Left(s"Invalid nucleotide $nuc")
    }
  }

  implicit val compShow: Show[Composition] = (t: Composition) => s"A: ${t.A} C: ${t.C}, G: ${t.G}, T:${t.T}"

  def exercise1(input:String):Either[String, Composition] = {
    input.foldLeft(Composition(0,0,0,0).asRight[String]){
      case (Left(x), _) => Left(x)
      case (Right(z), nuc) => z.add(nuc)
    }
  }

}
