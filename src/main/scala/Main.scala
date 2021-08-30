import cats.effect.{ExitCode, IO, IOApp}
import exercises.rosalind.ex1

object Main extends IOApp {


  def launch(args: List[String]): IO[ExitCode] = args match {
    // TODO poor man's CLI version but ¯\_(ツ)_/¯¯
    case "rosalind"::"ex1"::exercise_args => ex1.run(exercise_args)
    case _ => IO.println(s"Invalid module to execute: $args") *> IO(ExitCode.Error)
  }

  override def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case None => IO.println("Usage: run exercise").as(ExitCode.Error)
      case Some(_) => launch(args)
    }

}
