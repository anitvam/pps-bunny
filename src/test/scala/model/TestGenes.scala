package model

import org.scalatest.{FlatSpec, Matchers}

class TestGenes extends FlatSpec with Matchers {
  "Each AlleleKind" should "be in max one GeneKind" in {
    AlleleKind.values.foreach(anyAlleleKind => {
      assert(GeneKind.values.flatMap(gk => List(gk.base, gk.mutated))
        .filter(_ == anyAlleleKind)
        .size <= 1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    GeneKind.values.foreach(gk => assert(gk.base != gk.mutated))
  }

  "Any Allele " should "produce an uppercase letter if dominant " in {
    val geneKind = GeneKind.FUR_COLOR
    val dominantAllele = Allele(geneKind.base, Option(true))
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter).toCharArray()(0).isUpper)
  }

  it should "produce a lowercase letter if recessive" in {
    val geneKind = GeneKind.FUR_COLOR
    val dominantAllele = Allele(geneKind.base, Option(false))
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter).toCharArray()(0).isLower)
  }

  it should "not produce letters if the dominance is not defined yet" in {
    val geneKind = GeneKind.FUR_COLOR
    val dominantAllele = Allele(geneKind.base)
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter) == "")
  }

  "Any Gene" should "should throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[IllegalAlleleException] {
      Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.LONG_FUR), Allele(AlleleKind.BROWN_FUR))
    }
  }

  it should "should be initialized with Alleles of the right kind" in {
    noException should be thrownBy Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.WHITE_FUR))
  }
}
