package example.error_handling

import cats.effect.{ExitCode, IO}

object common {
  case class Url(data:String)
  case class JWT(token:String)
  case class Content(body:String)
  case class Link(data:String)
  case class Headers(data:Map[String, String])

  val JWT_PARAM:JWT = JWT(
    """eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikpv
      |aG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c""".stripMargin)

  val URL_PARAM:Url = Url("www.gmail.com")

  def runSpyder(spyder:(JWT, Url) => String):IO[ExitCode] = {
    IO.println(spyder(JWT_PARAM, URL_PARAM)) *> IO(ExitCode.Success)
  }

  sealed abstract class SpyderException extends Exception
  case class ExternalException() extends SpyderException
  case class ParseException() extends SpyderException

}
