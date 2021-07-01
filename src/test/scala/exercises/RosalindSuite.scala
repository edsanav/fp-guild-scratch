package exercises

import exercises.rosalind.{Composition, exercise1}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks


class RosalindSuite extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with Checkers {

  val nucleotideGen:Gen[Char] = Gen.oneOf('A', 'C', 'G', 'T')
  val nonNucleotide:Gen[Char] = Arbitrary.arbitrary[Char].filterNot( c => c =='A' | c=='C' | c=='G' | c=='T')
  val DNAGen:Gen[String] = Gen.stringOf(nucleotideGen)
  val DNAGenWithNotDNA:Gen[String] = DNAGen.map(s => s + nonNucleotide)
  val noDNA:Gen[String] = Gen.stringOf(nonNucleotide)

  def definedFreq(A:Int, C:Int, G: Int, T:Int):Gen[String] = Gen.stringOfN(1000, Gen.frequency(
    (A, Gen.const('A')),(C,Gen.const('C')), (G, Gen.const('G')), (T, Gen.const('T'))
  ))


  test("Invariance") {
    forAll(DNAGen) { dna:String =>
      exercise1(dna).isRight
    }
  }

  test("Invariance left") {
    forAll(DNAGenWithNotDNA) { dna:String =>
      exercise1(dna).isLeft
    }
  }

  //https://github.com/typelevel/scalacheck/blob/main/doc/UserGuide.md
  test("Expected frequencies"){
    val x = 5
    forAll(definedFreq(x, 2*x, 3*x, 4*x)){ (dna:String) =>
      exercise1(dna) match {
        case Right(c:Composition) =>println(c.C.toFloat / c.A); assert((c.C.toFloat / c.A) == 2.0)
        case _ => fail("Not a valid output")
      }
    }

  }


}