package example.error_handling

import cats.effect.{ExitCode, IO}
import common._

object happy {

  
  def buildHeaders(jwt:JWT):Headers = Headers(Map("Authorization"->jwt.token))
  
  def getContent(u:Url, h:Headers):Content =
    Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com")
  
  def extractLinks(c:Content): List[Link] ={
    c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s))
  }


  def runSpyder(creds:JWT, url: Url):List[Link] = {
    val headers = buildHeaders(creds)
    val content = getContent(url, headers)
    extractLinks(content)
  }

  def run():IO[ExitCode] = {
    IO.println(runSpyder(JWT_PARAM, URL_PARAM).mkString("\n")) *> IO(ExitCode.Success)
  }

}
