package example.error_handling

import cats.effect.{ExitCode, IO}
import example.error_handling.common.{Content, Headers, JWT, Link, Url}

object option {

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


  def runImperative():IO[ExitCode] = {
    val token = JWT(
      """eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikpv
        |aG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c""".stripMargin)
    val url = Url("www.gmail.com")
    IO.println(runSpyder(token, url).mkString("\n")) *> IO(ExitCode.Success)
  }


}
