package example.error_handling

import example.error_handling.common.{Content, Headers, JWT, Link, Url}

import scala.annotation.unused
import scala.util.{Failure, Success, Try}

object trym {

//  sealed trait Try[+T]
//  case class Failure[+T](exception: Throwable) extends Try[T]
//  case class Success[+T](value: T) extends Try[T]

  // Wrap error that can throw exceptions. Error type is fixed to throwable

  def buildHeaders(jwt:JWT):Try[Headers] = Try(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Try[Content] = {
    Try(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Try[Content] = Try(throw new RuntimeException("BOOOOOOOOM"))

  def extractLinks(c:Content): Try[List[Link]] ={
    Try(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  def tryOk(creds:JWT, url: Url):String = {
    val result  = for {
      headers <- buildHeaders(creds)
      content <- getContent(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Success(result) => result
      case Failure(th) =>  s"An expected error occured during the execution: ${th.getMessage}"
    }
  }

  def tryKO(creds:JWT, url: Url):String = {
    val result  = for {
      headers <- buildHeaders(creds)
      content <- getContentBroken(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Success(result) => result
      case Failure(th) =>  s"An expected error occured during the execution: ${th.getMessage}"
    }
  }



}
