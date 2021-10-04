package example.error_handling

import example.error_handling.common.{Content, Headers, JWT, JWT_PARAM, Link, URL_PARAM, Url}

import scala.annotation.unused

object option {

  // MONADS!
  // A monad is:
  // a) a Burrito
  // b) a Box
  // c) Just monoid in the category of endofunctors (whatever that means)
  // d) Something that provides unit (a constructor), flatMap and follow some rules
  // e) All of the above are correct (kind of)

  /**
   * Monads are nothing more than a mechanism to sequence computations around values augmented with some additional
   * feature. Such features are called effects.
   * Some well-known effects are managing the nullability of a variable or managing the asynchronicity of its computation.
   * In Scala, the corresponding monads to these effects are the Option[T] type and the Future[T] type.
   *
   * Wrapping of the value inside the effect is done through the unit method unit: A => F[A] F[A] f: F[A]
   * Sequencing of the effects are provide by flatMap (given fa:F[A] and f: A => F[B], return F[B]
   *
   * More visual: https://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html#monads
   */

//  sealed trait Option[+A]
//  case class Some[+A](value: A) extends Option[A]
//  case object None extends Option[Nothing]

  //Value or abscence of value
  def buildHeaders(jwt:JWT):Option[Headers] = Some(Headers(Map("Authorization"->jwt.token)))

  def getContent(u:Url, h:Headers):Option[Content] = {
    Some(Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps://www.github.com"))
  }

  def getContentBroken(@unused u:Url, @unused h:Headers):Option[Content] = None

  def extractLinks(c:Content): Option[List[Link]] ={
    Some(c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s)))
  }

  //...Ok, how can we use Option in an imperative way (without leveraging the monad).
  // Kind of messy...with...vars... :S
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

  // Using monads
  def monadSpyderOK():String = {
    val result = for {
      headers <- buildHeaders(JWT_PARAM)
      content <- getContent(URL_PARAM, headers)
      links <- extractLinks(content)
    } yield links.mkString("\n")

    //  De-sugarized version A => F[B]
    // val result = buildHeaders(JWT_PARAM).flatMap(h => getContent(URL_PARAM, h)).flatMap(c => extractLinks(c)).map(_.mkString)

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
