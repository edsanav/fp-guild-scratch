package example.error_handling

import example.error_handling.common._

import scala.annotation.unused

object either {

  //sealed abstract class Either[+A, +B]
  //case class Left[+A, +B](value: A) extends Either[A, B]
  //case class Right[+A, +B](value: B) extends Either[A, B]

  // Value or error (can be a custom type)

  sealed trait SpyderException
  case class AuthException() extends SpyderException
  case class ExternalException(code:Int) extends SpyderException
  case class ParseException(msg:String) extends SpyderException

  def buildHeaders(jwt:JWT):Either[AuthException, Headers] = Right(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Either[ExternalException, Content] = {
    Right(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps//www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Either[ExternalException, Content] =
    Left(ExternalException(500))

  def extractLinks(c:Content): Either[ParseException, List[Link]] ={
    Right(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  def eitherOK():String = {
    val result  = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContent(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Right(result) => result
      case Left(error) =>  s"An expected error occured during the execution: $error"
    }
  }

  def eitherKO():String = {
    val result  = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContentBroken(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")
    result match {
      case Right(result) => result
      case Left(error) =>  s"An expected error occured during the execution: $error"
    }
  }


  case class Person(name:String, email:String, passport:String)

  def validateName(name:String):Either[String, String] = Right(name)

  def validateEmail(email:String): Either[String, String] = Right(email)

  def validatePassportNum(num:String): Either[String, String] = Right(num)

  def validateNameFail(@unused name:String):Either[String, String] = Left("Error: name")

  def validateEmailFail(@unused email:String): Either[String, String] = Left("Error: email")

  def validatePassportNumFail(@unused num:String): Either[String, String] =  Left("Error: passport num")


  def eitherFormOK():String = {
    val result  = for {
      name <- validateName("Paco")
      email <- validateEmail("paco@pil.com")
      passport <- validatePassportNum("12345")
    } yield Person(name, email, passport)

    result match {
      case Right(p) => s"Valid person entry: ${p}"
      case Left(errors) => s"There were some errors: ${errors}"
    }
  }

  def eitherFormKO():String = {
    val result  = for {
      name <- validateNameFail("Paco")
      email <- validateEmailFail("paco@pil.com")
      passport <- validatePassportNumFail("12345")
    } yield Person(name, email, passport)

    result match {
      case Right(p) => s"Valid person entry: ${p}"
      case Left(errors) => s"There were some errors: ${errors}"
    }
  }


}
