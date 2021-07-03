package exercises

import cats.Show
import cats.effect.{ExitCode, IO}
import cats.implicits._

object rosalind {


  def run(args:List[String]):IO[ExitCode] = {
    val result = args match {
      case "ex1"::input::_ => ntComposition(input).map(_.show)
      case _ => Left("Invalid input")
    }
    result match  {
      case Right(out) => IO.println(out) *> IO(ExitCode.Success)
      case Left(err) => IO.println(s"Errors while executing exercise: $err") *> IO(ExitCode.Error)

    }
  }

  case class Composition(A:Int, C:Int, G:Int, T:Int) {
    def +(c: Composition):Composition = Composition(A+c.A, C+c.C, G+c.G, T+c.T)
  }

  object Composition {
    def parse(nt:Char):Either[String, Composition] = nt match {
      case 'A' => Right(Composition(1, 0, 0, 0))
      case 'C'=>Right(Composition(0, 1, 0, 0))
      case 'G' => Right(Composition(0, 0, 1, 0))
      case 'T'=> Right(Composition(0,0,0,1))
      case _ => Left(s"Invalid nucleotide $nt\n")
    }
  }

  implicit val compShow: Show[Composition] = (t: Composition) => s"A: ${t.A} C: ${t.C}, G: ${t.G}, T:${t.T}"

  def ntComposition(input:String):Either[String, Composition] = {
    input.foldLeft(Composition(0,0,0,0).asRight[String]){
      case (Left(x), _) => Left(x) // only one error is carried
      case (Right(z), nt) => Composition.parse(nt) match {
        case Right(c) => Right(z+c)
        case x => x
      }
    }
  }

//  def ntCompositionErrors(input:String):Either[String, Composition] = {
//    input.foldLeft(Composition(0,0,0,0).asRight[String]){
//      case (Left(x), _) => Left(x) // only one error is carried
//      case (Right(z), nt) => Right(z + Composition.parse(nt))
//    }
//  }

}
