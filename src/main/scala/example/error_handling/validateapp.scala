package example.error_handling

import cats.{Semigroup, Semigroupal}
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._

import scala.annotation.unused

object validateapp {


  // When we don't want sequence of operations (imagine a form).
  // Similar to Either but being an applicative
  // https://medium.com/@lettier/your-easy-guide-to-monads-applicatives-functors-862048d61610

  case class Person(name:String, email:String, passport:String)

  def validateName(name:String):Validated[String, String] = Valid(name)

  def validateEmail(email:String): Validated[String, String] = Valid(email)

  def validatePassportNum(num:String): Validated[String, String] = Valid(num)

  def validateNameFail(@unused name:String):Validated[String, String] = Invalid("Error: name")

  def validateEmailFail(@unused email:String): Validated[String, String] = Invalid("Error: email")

  def validatePassportNumFail(@unused num:String): Validated[String, String] =  Invalid("Error: passport num")


  def validatedOK():String = {
    val result:Validated[String, Person] = {
      (
        validateName("Paco"),
        validateEmail("paco@pil.com"),
        validatePassportNum("1234")
      ).mapN(Person)
    }

    result match {
      case Valid(p) => s"Valid person entry: ${p}"
      case Invalid(errors) => s"There were some errors: ${errors}"
    }

  }

  def validatedKO():String = {
    val result:Validated[String, Person] = {
      (
        validateName("Paco"),
        validateEmailFail("paco@pil.com"),
        validatePassportNumFail("1234")
        ).mapN(Person)
    }

    result match {
      case Valid(p) => s"Valid person entry: ${p}"
      case Invalid(errors) => s"There were some errors: ${errors}"
    }

  }

  def toy():Unit = {
    val firstS: Validated[String, String] = Invalid("err1")
    val secondS: Validated[String, String] = Valid("ok")
    val thirdS: Validated[String, String] = Invalid("err1")

    val result = Semigroup[Validated[String, String]].combine(firstS, secondS).combine(thirdS)

    val result2 = Semigroupal.map3(firstS, secondS, thirdS){case (a,b,c) => (a,b,c)}
    println(result)
    println(result2)
  }
}
