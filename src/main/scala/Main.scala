import cats.effect.{ExitCode, IO, IOApp}
import example.error_handling.common.runProgram
import exercises.rosalind.{ex1, ex2}
import example.error_handling._

object Main extends IOApp {


  def launch(args: List[String]): IO[ExitCode] = args match {
    // TODO poor man's CLI version but ¯\_(ツ)_/¯¯
    case "rosalind"::module_args => rosalind(module_args)
    case "error_handling"::module_args => error_handling(module_args)
    case _ => IO.println(s"Invalid module to execute: $args") *> IO(ExitCode.Error)
  }

  def rosalind(args:List[String]): IO[ExitCode] = args match {
    case "ex1"::exercise_args => ex1.run(exercise_args)
    case "ex2"::exercise_args => ex2.run(exercise_args)
    case _ => IO.println(s"Invalid exercise to execute: $args") *> IO(ExitCode.Error)
  }

  def error_handling(args: List[String]): IO[ExitCode] = args match {
    case "happy"::_ => runProgram(happy.spyder)
    case "exceptions"::"ok"::_ => runProgram(exceptions.spyderCatch)
    case "exceptions"::"ko"::_ => runProgram(exceptions.spyderYOLO)
    case "option"::"imperative"::"ok"::_ => runProgram(option.imperativeSpyderOK)
    case "option"::"imperative"::"ko"::_ => runProgram(option.imperativeSpyderKO)
    case "option"::"monad"::"ok"::_ => runProgram(option.monadSpyderOK)
    case "option"::"monad"::"ko"::_ => runProgram(option.monadSpyderKO)
    case "either"::"form"::"ok"::_ => runProgram(either.eitherFormOK)
    case "either"::"form"::"ko"::_ => runProgram(either.eitherFormKO)
    case "either"::"ok"::_ => runProgram(either.eitherOK)
    case "either"::"ko"::_ => runProgram(either.eitherKO)
    case "validated"::"ok"::_ => runProgram(validateapp.validatedOK)
    case "validated"::"ko"::_ => runProgram(validateapp.validatedKO)
    case "toy"::_ => IO(validateapp.toy()) *> IO(ExitCode.Success)

    case _ => IO.println(s"Invalid exercise to execute: $args") *> IO(ExitCode.Error)
  }

  override def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case None => IO.println("Usage: run exercise").as(ExitCode.Error)
      case Some(_) => launch(args)
    }

}
