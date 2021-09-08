package example.error_handling

import example.error_handling.common.{Content, Headers, JWT, Link, Url}

import scala.annotation.unused

object option {

  def buildHeaders(jwt:JWT):Option[Headers] = Some(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Option[Content] = {
    Some(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Option[Content] = None

  def extractLinks(c:Content): Option[List[Link]] ={
    Some(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  def imperativeSpyderOK(creds:JWT, url: Url):String = {
    var links: Option[List[Link]] = None // that "var" there: https://c.tenor.com/JHPSRMwQkCAAAAAC/elmo-hell.gif
    val headers = buildHeaders(creds)
    if (headers.isDefined){
      val content = getContent(url, headers.get) // never do ".get" directly unless you want a good exception on None
      if (content.isDefined){
        links = extractLinks(content.get)
      }
    }
    if (links.isDefined){
      links.get.mkString("\n")
    }else{
      "An error occured during the execution"
    }
  }

  def imperativeSpyderKO(creds:JWT, url: Url):String = {
    var result: Option[String] = None // that "var" there: https://c.tenor.com/JHPSRMwQkCAAAAAC/elmo-hell.gif
    val headers = buildHeaders(creds)
    if (headers.isDefined){
      val content = getContentBroken(url, headers.get) // never do ".get" directly (or you get exceptions on None)
      if (content.isDefined){
        val links = extractLinks(content.get)
        if (links.isDefined){
          result = Some(links.get.mkString("\n"))
        }
      }
    }
    if (result.isDefined){
      result.get
    }else{
      "An error occured during the execution"
    }
  }

  //TODO link here
  def monadSpyderOK(creds:JWT, url: Url):String = {
    val result = for {
      headers <- buildHeaders(creds)
      content <- getContent(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")

    //  De-sugarized version
    //  val result = buildHeaders(creds).flatMap(h => getContent(url, h)).flatMap(c => extractLinks(c)).map(_.mkString)

    result match {
      case Some(result) => result
      case None =>  "An expected error occured during the execution"
    }
  }

  def monadSpyderKO(creds:JWT, url: Url):String =  {
    val result = for {
      headers <- buildHeaders(creds)
      content <- getContentBroken(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")

    result match {
      case Some(result) => result
      case None => "An expected error occured during the execution"
    }
  }





}
