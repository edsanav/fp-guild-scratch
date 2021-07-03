package exercises

import exercises.rosalind.{Composition, ntComposition}
import org.scalacheck.Prop.{forAll, forAllNoShrink}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers


class RosalindSuite extends AnyFunSuite  with Checkers {

  val nucleotideGen:Gen[Char] = Gen.oneOf('A', 'C', 'G', 'T')
  val nonNucleotide:Gen[Char] = Arbitrary.arbitrary[Char].filterNot( c => c =='A' | c=='C' | c=='G' | c=='T')
  val DNAGen:Gen[String] = Gen.stringOf(nucleotideGen)
  val nonDNA:Gen[String] = nonEmptyStringOf(Arbitrary.arbitrary[Char])

  def nonEmptyStringOf(g:Gen[Char]):Gen[String] = Gen.nonEmptyListOf[Char](g).map(_.mkString)

  test("Invariance") {
    forAll(DNAGen) { dna:String =>
      ntComposition(dna).isRight
    }
  }

  test("Invariance left") {
    // Non empty string because an empty one would return a valid composition response
    forAll(nonDNA){ nonDna:String =>
      ntComposition(nonDna).isLeft
    }
  }

  test("length must be equal to total sum"){
    check(forAllNoShrink(DNAGen){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) => println(dna.length); dna.length == (c.A + c.C + c.G + c.T)
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("only A"){
    check(forAllNoShrink(nonEmptyStringOf(Gen.const('A'))){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) => c.A == dna.length && c.C == 0 && c.G == 0 && c.T == 0
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("only C"){
    check(forAllNoShrink(nonEmptyStringOf(Gen.const('C'))){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) => c.C == dna.length && c.A == 0 && c.G == 0 && c.T == 0
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("only G"){
    check(forAllNoShrink(nonEmptyStringOf(Gen.const('G'))){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) => c.G == dna.length && c.A == 0 && c.C == 0 && c.T == 0
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("only T"){
    check(forAllNoShrink(nonEmptyStringOf(Gen.const('T'))){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) => c.T == dna.length && c.A == 0 && c.C == 0 && c.G == 0
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

//  test("Expected A"){
//    val x = 5
//    forAll(definedFreq(x, x, x, x)){ (dna:String) =>
//      ntComposition(dna) match {
//        case Right(c:Composition) =>println(c.C.toFloat / c.A); assert((c.C.toFloat / c.A) == 2.0)
//        case _ => fail("Not a valid output")
//      }
//    }
//
//  }


}