package exercises.rosalind

import cats.effect.{ExitCode, IO}

object rna {

  def run(input:String):IO[ExitCode] = IO.println(input.replace("T","U")) *> IO(ExitCode.Success)

}
