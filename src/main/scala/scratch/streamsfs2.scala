package scratch

import cats.effect.{ExitCode, IO}
import fs2.io.file.{Files, Path}
import fs2.{io, text}

object streamsfs2 {
  def run(args: List[String]): IO[ExitCode] = {
    args match {
      case input :: _ => loadContent(input).compile.drain *> IO(ExitCode.Success)
      case _ => IO.println(s"Errors while executing exercise: Invalid input") *> IO(ExitCode.Error)
    }
  }

  //  Files[IO].readAll(Path("testdata/fahrenheit.txt"))
  //    .through(text.utf8.decode)
  //    .through(text.lines)
  //    .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
  //    .map(line => fahrenheitToCelsius(line.toDouble).toString)
  //    .intersperse("\n")
  //    .through(text.utf8.encode)
  //    .through(Files[IO].writeAll(Path("testdata/celsius.txt")))

  def loadContent(p: String) =
    Files[IO]
      .readAll(Path(p))
      .through(text.utf8.decode)
      .through(text.lines)
      .through(text.utf8.encode)
      .through(io.stdout)


}
