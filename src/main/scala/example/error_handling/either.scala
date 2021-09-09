package example.error_handling

import example.error_handling.common._

import scala.annotation.unused
import scala.util.{Success, Try}

object either {

  //sealed abstract class Either[+A, +B]
  //case class Left[+A, +B](value: A) extends Either[A, B]
  //case class Right[+A, +B](value: B) extends Either[A, B]

  //you can define left type
  sealed trait SpyderException
  case class ExternalException(code:Int) extends SpyderException
  case class ParseException() extends SpyderException

  def buildHeaders(jwt:JWT):Either[SpyderException, Headers] = Right(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Either[ExternalException, Content] = {
    Right(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Either[ExternalException, Content] =
    Left(ExternalException(500))

  def extractLinks(c:Content): Either[String, List[Link]] ={
    Right(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }



  def eitherOK(creds:JWT, url: Url):String = {
    val result  = for {
      headers <- buildHeaders(creds)
      content <- getContent(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Right(result) => result
      case Left(error) =>  s"An expected error occured during the execution: $error"
    }
  }

  def eitherKO(creds:JWT, url: Url):String = {
    val result  = for {
      headers <- buildHeaders(creds)
      content <- getContentBroken(url, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Right(result) => result
      case Left(error) =>  s"An expected error occured during the execution: $error"
    }
  }


}
