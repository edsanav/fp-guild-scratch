package example.error_handling

import example.error_handling.common.{Content, Headers, JWT, JWT_PARAM, Link, URL_PARAM, Url}

import scala.annotation.unused

object option {

//  sealed trait Option[+A]
//  case class Some[+A](value: A) extends Option[A]
//  case object None extends Option[Nothing]

  //Value or abscence of value

  def buildHeaders(jwt:JWT):Option[Headers] = Some(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Option[Content] = {
    Some(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Option[Content] = None

  def extractLinks(c:Content): Option[List[Link]] ={
    Some(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  def imperativeSpyderOK():String = {
    var links: Option[List[Link]] = None // that "var" there: https://c.tenor.com/JHPSRMwQkCAAAAAC/elmo-hell.gif
    val headers = buildHeaders(JWT_PARAM)
    if (headers.isDefined){
      val content = getContent(URL_PARAM, headers.get) // never do ".get" directly unless you want a good exception on None
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

  def imperativeSpyderKO():String = {
    var result: Option[String] = None // that "var" there: https://c.tenor.com/JHPSRMwQkCAAAAAC/elmo-hell.gif
    val headers = buildHeaders(JWT_PARAM)
    if (headers.isDefined){
      val content = getContentBroken(URL_PARAM, headers.get) // never do ".get" directly (or you get exceptions on None)
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
  def monadSpyderOK():String = {
    val result = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContent(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")

    //  De-sugarized version
    //  val result = buildHeaders(JWT_PARAM).flatMap(h => getContent(URL_PARAM, h)).flatMap(c => extractLinks(c)).map(_.mkString)

    result match {
      case Some(result) => result
      case None =>  "An expected error occured during the execution"
    }
  }

  def monadSpyderKO():String =  {
    val result = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContentBroken(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")

    result match {
      case Some(result) => result
      case None => "An expected error occured during the execution"
    }
  }





}
