package exercises.rosalind

import cats.Show
import cats.effect.{ExitCode, IO}
import cats.implicits._

object ex1 {


  def run(args:List[String]):IO[ExitCode] = {
    val result = args match {
      case input::_ => ntComposition(input).map(_.show)
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
      case _ => Left(s"  Invalid nucleotide $nt")
    }
  }

  implicit val CompositionCanShow: Show[Composition] = (t: Composition) => s"A: ${t.A} C: ${t.C}, G: ${t.G}, T:${t.T}"

  def ntComposition(input:String):Either[String, Composition] = {
    input.foldLeft(Composition(0,0,0,0).asRight[String]){
      case (Left(x), _) => Left(x) // only one error is carried
      case (Right(z), nt) => Composition.parse(nt) match {
        case Right(c) => Right(z+c)
        case x => x
      }
    }
  }

  def ntCompositionCarryErrors(input:String):Either[String, Composition] = {
    input.map(Composition.parse).foldLeft(Composition(0,0,0,0).asRight[String]){
      case (Right(z), Right(b)) => Right(z + b)
      case (Left(z), Left(x)) => Left(s"$z\n$x")
      case (Left(x), _) => Left(x)
      case (_, Left(x)) => Left(x)
    }
  }

}
