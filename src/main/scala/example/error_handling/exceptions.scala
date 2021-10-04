package example.error_handling

import common._

object exceptions {

  def buildHeaders(jwt:JWT):Headers = Headers(Map("Authorization"->jwt.token))

  // Here we mimick an error occurred remotely. This is not RT
  def getContent(u:Url, h:Headers):Content = {
    val _ = Content(s"Random Stuff from ${u} headers ${h}\nhttps://www.google.com\nhttps://www.github.com")
    throw new RuntimeException("BOOOOOOOOOOOOOOOM No content today")
  }

  def extractLinks(c:Content): List[Link] ={
    c.body.split("\n").toList.filter(_.startsWith("https")).map(s=>Link(s))
  }

  // Without handling exceptions
  def spyderYOLO():String = {
    val headers = buildHeaders(JWT_PARAM)
    val content = getContent(URL_PARAM, headers)
    val links = extractLinks(content)
    links.mkString("\n")
  }

  // Handling exceptions
  def spyderCatch():String = {
    try{
      val headers = buildHeaders(JWT_PARAM)
      val content = getContent(URL_PARAM, headers)
      val links = extractLinks(content)
      links.mkString("\n")
    }catch {
      case e: RuntimeException => s"An (expected) error ocurred: ${e.getMessage}"
    }
  }

  // Fine, this is an expression and can be translated to the corresponding value...BUT:

  /**
   * https://blog.rockthejvm.com/idiomatic-error-handling-in-scala/
   * However, this straightforward approach is rarely the recommended one, for a few big reasons:
   *  - the structure is cumbersome and hard to read, particularly if the attempted code or the exception-handling code becomes big
   *  - nesting such structures (even at level 2) become exponentially harder to debug and understand
   *  - for pure functional programmers, the finally part doing things outside of value computations might cause a small aneurysm
   */

}
