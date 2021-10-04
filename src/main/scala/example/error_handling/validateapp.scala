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
  // https://softwaremill.com/applicative-functor/
  // https://typelevel.org/cats/typeclasses/applicative.html

  /**
   * https://medium.com/@lettier/your-easy-guide-to-monads-applicatives-functors-862048d61610
   * "Applicative functor picks up where functor leaves off. Functor lifts/upgrades a function making it capable
   * of operating on a single effect. Applicative functor allows the sequencing of multiple independent effects.
   * Functor deals with one effect while applicative functor can deal with multiple independent effects.
   *
   * In other words, applicative functor generalizes functor."
   *
   * Applicative extends Functor with an ap F[A=>B], F[A] -> F[B] and pure method. A => F[A]
   * */


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

  /**
   * https://blog.rockthejvm.com/idiomatic-error-handling-in-scala/#4-advanced-validated
   *
   * If all are valid, their wrapped values will combine as specified by the implicit Semigroup of that type (basically a combination function).
   * If some are invalid, the result will be an Invalid instance containing the combination of all the errors as specified by the implicit Semigroup for the error type;
   *
   * Validated (...) allows us to
   * combine multiple errors into one instance, thus creating a comprehensive report
   * process both values and errors, separately or at the same time
   * convert to/from Either, Try and Option
   *
   */

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
