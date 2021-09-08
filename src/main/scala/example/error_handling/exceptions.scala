package example.error_handling

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

  def spyderYOLO(creds:JWT, url: Url):String = {
    val headers = buildHeaders(creds)
    val content = getContent(url, headers)
    val links = extractLinks(content)
    links.mkString("\n")
  }

  def spyderCatch(creds:JWT, url:Url):String = {
    try{
      val headers = buildHeaders(creds)
      val content = getContent(url, headers)
      val links = extractLinks(content)
      links.mkString("\n")
    }catch {
      case e: RuntimeException => s"An (expected) error ocurred: ${e.getMessage}"
    }
  }

}
