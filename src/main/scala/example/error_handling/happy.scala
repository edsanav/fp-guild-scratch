package example.error_handling

import common._

object happy {

  
  def buildHeaders(jwt:JWT):Headers = Headers(Map("Authorization"->jwt.token))
  
  def getContent(u:Url, h:Headers):Content =
    Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com")
  
  def extractLinks(c:Content): List[Link] ={
    c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s))
  }


  def spyder(creds:JWT, url: Url):String = {
    val headers = buildHeaders(creds)
    val content = getContent(url, headers)
    val links = extractLinks(content)
    links.mkString("\n")
  }

}
