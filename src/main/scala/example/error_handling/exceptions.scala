package example.error_handling

import cats.effect.{ExitCode, IO}

object exceptions {

  // Effects https://youtu.be/30q6BkBv5MY?t=993

  case class Url()
  case class Credentials()
  case class Content()
  case class Links()


  def getLinks():List[Links] = {
    val u = Url()
    val c = Client()
    c.get()

  }



  def run():IO[ExitCode] = {
    IO(getLinks()) *> IO(ExitCode.Success)
  }

}
