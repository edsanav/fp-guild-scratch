package example.error_handling

import example.error_handling.common.{Content, Headers, JWT, JWT_PARAM, Link, URL_PARAM, Url}

import scala.annotation.unused
import scala.util.{Failure, Success, Try}

object trym {


  // OK, ok, but absence of value does not provide much information
  // Plus, what happens if there are parts of the code that throw exceptions, not handled by us

  // Try to the rescue! Effect: value or error (throwable)

  // Bear in mind now the "error" type includes a throwable.

  //  sealed trait Try[+T]
  //  case class Failure[+T](exception: Throwable) extends Try[T]
  //  case class Success[+T](value: T) extends Try[T]

  // Wrap error that can throw exceptions. Error type is fixed to throwable

  def buildHeaders(jwt:JWT):Try[Headers] = Try(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Try[Content] = {
    Try(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps://www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Try[Content] = Try(throw new RuntimeException("BOOOOOOOOM"))

  def extractLinks(c:Content): Try[List[Link]] ={
    Try(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  def tryOk():String = {
    val result  = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContent(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Success(result) => result
      case Failure(th) =>  s"An expected error occured during the execution: ${th.getMessage}"
    }
  }

  def tryKO():String = {
    val result  = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContentBroken(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Success(result) => result
      case Failure(th) =>  s"An expected error occured during the execution: ${th.getMessage}"
    }
  }



}
