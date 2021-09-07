package example.error_handling

import cats.effect.{ExitCode, IO}
import common._

object exceptions {

  def buildHeaders(jwt:JWT):Headers = Headers(Map("Authorization"->jwt.token))
  
  def getContent(u:Url, h:Headers):Content = {
    val _ = Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com")
    throw new RuntimeException("BOOOOOOOOOOOOOOOM No content today")
  }

  def extractLinks(c:Content): List[Link] ={
    c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s))
  }

  def runSpyder(creds:JWT, url: Url):List[Link] = {
    val headers = buildHeaders(creds)
    val content = getContent(url, headers)
    extractLinks(content)
  }

  def runYOLO():IO[ExitCode] = {
    IO.println(runSpyder(JWT_PARAM, URL_PARAM).mkString("\n")) *> IO(ExitCode.Success)
  }

  def runOK():IO[ExitCode] = {
    try{
      IO.println(runSpyder(JWT_PARAM, URL_PARAM).mkString("\n")) *> IO(ExitCode.Success)
    } catch {
      case e: RuntimeException => IO.println(s"An error ocurred: $e") *> IO(ExitCode.Error)
    }

  }

}
