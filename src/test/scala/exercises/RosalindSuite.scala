package exercises

import exercises.rosalind.{Composition, ntComposition, ntCompositionCarryErrors}
import org.scalacheck.Prop.forAllNoShrink
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers


class RosalindSuite extends AnyFunSuite  with Checkers {

  val nucleotideGen:Gen[Char] = Gen.oneOf('A', 'C', 'G', 'T')
  val nonNucleotide:Gen[Char] = Arbitrary.arbitrary[Char].filterNot( c => c =='A' | c=='C' | c=='G' | c=='T')
  val DNAGen:Gen[String] = Gen.stringOf(nucleotideGen)

  val nonDNALong:Gen[String] = for {
   len <- Gen.choose(100, 1000)
   s <- Gen.stringOfN(len, Arbitrary.arbitrary[Char])
  } yield s

  test("ntComposition: Invariance") {
    check(forAllNoShrink(DNAGen) { dna:String =>
      ntComposition(dna).isRight
    })
  }

  test("ntComposition: Invariance left") {
    // Non empty string because an empty one would return a valid composition response
    check(forAllNoShrink(nonDNALong){ nonDna:String =>
      ntComposition(nonDna).isLeft
    }
    )
  }

  test("ntComposition: Length must be equal to total sum"){
    check(forAllNoShrink(DNAGen){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) =>  dna.length == (c.A + c.C + c.G + c.T)
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("ntComposition: Length of each separated nt must be equal to amount"){
    check(forAllNoShrink(DNAGen){ dna:String =>
      ntComposition(dna) match {
        case Right(c:Composition) =>
          dna.count(_=='A') == c.A && dna.count(_=='C') == c.C && dna.count(_=='G') == c.G && dna.count(_=='T') == c.T
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("ntCompositionCarryErrors: Invariance") {
    check(forAllNoShrink(DNAGen) { dna:String =>
      ntCompositionCarryErrors(dna).isRight
    })
  }

  test("ntCompositionCarryErrors: Invariance left") {
    // Non empty string because an empty one would return a valid composition response
    check(forAllNoShrink(nonDNALong){ nonDna:String =>
      ntCompositionCarryErrors(nonDna).isLeft
    }
    )
  }

  test("ntCompositionCarryErrors: Length must be equal to total sum"){
    check(forAllNoShrink(DNAGen){ dna:String =>
      ntCompositionCarryErrors(dna) match {
        case Right(c:Composition) =>  dna.length == (c.A + c.C + c.G + c.T)
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("ntCompositionCarryErrors: Length of each separated nt must be equal to amount"){
    check(forAllNoShrink(DNAGen){ dna:String =>
      ntCompositionCarryErrors(dna) match {
        case Right(c:Composition) =>
          dna.count(_=='A') == c.A && dna.count(_=='C') == c.C && dna.count(_=='G') == c.G && dna.count(_=='T') == c.T
        case _ => fail(s"not a valid output $dna")
      }
    })
  }

  test("ntCompositionCarryErrors: Invariance left has one line per error") {
    // Non empty string because an empty one would return a valid composition response
    check(forAllNoShrink(nonDNALong){ nonDna:String =>
      ntCompositionCarryErrors(nonDna) match {
        case Left(err) =>
          err.split("\n").length == nonDna.filterNot( c => c =='A' | c=='C' | c=='G' | c=='T').length
        case _ => fail(s"expected $nonDna")
      }
    }
    )
  }



}