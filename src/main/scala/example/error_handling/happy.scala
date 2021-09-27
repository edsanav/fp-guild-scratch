package example.error_handling

import common._

object happy {


  // Fake spyder: Just build headers, connect to URL and extract links of the content

  def buildHeaders(jwt:JWT):Headers = Headers(Map("Authorization"->jwt.token))
  
  def getContent(u:Url, h:Headers):Content =
    Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com")
  
  def extractLinks(c:Content): List[Link] ={
    c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s))
  }

  // Everyone is happy with the happy path...
  def spyder():String = {
    val headers = buildHeaders(JWT_PARAM)
    val content = getContent(URL_PARAM, headers)
    val links = extractLinks(content)
    links.mkString("\n")
  }

}
