package model

import model.AllGenes.{AlleleKind, FUR_COLOR, FUR_LENGTH}
import org.scalatest.{FlatSpec, Matchers}

class TestGenes extends FlatSpec with Matchers {
  "Each AlleleKind" should "be in max one GeneKind" in {
    AlleleKind.values.foreach(anyAlleleKind => {
      assert(AllGenes.values.flatMap(gk => List(gk.base, gk.mutated)).count(_ == anyAlleleKind) <= 1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    AllGenes.values.foreach(gk => assert(gk.base != gk.mutated))
  }

  "Any Allele " should "produce an uppercase letter if dominant " in {
    val geneKind = AllGenes.FUR_COLOR
    val dominantAllele = Allele(geneKind.base, Option(true))
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter).toCharArray()(0).isUpper)
  }

  it should "produce a lowercase letter if recessive" in {
    val geneKind = AllGenes.FUR_COLOR
    val dominantAllele = Allele(geneKind.base, Option(false))
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter).toCharArray()(0).isLower)
  }

  it should "not produce letters if the dominance is not defined yet" in {
    val geneKind = AllGenes.FUR_COLOR
    val dominantAllele = Allele(geneKind.base)
    assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter) == "")
  }

  "Any Gene" should "throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[IllegalAlleleException] {
      Gene(AllGenes.FUR_COLOR, Allele(AlleleKind.LONG_FUR), Allele(AlleleKind.BROWN_FUR))
    }
  }

  it should " be initialized with Alleles of the right kind" in {
    noException should be thrownBy Gene(AllGenes.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.WHITE_FUR))
  }

  "Any Genotype" should "throw an Exception if it does not contain all kind of Genes" in {
    assertThrows[IllegalGenotypeException] {
      Genotype(Map( FUR_COLOR ->  Gene(AllGenes.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.BROWN_FUR)),
                    FUR_LENGTH -> Gene(AllGenes.FUR_LENGTH, Allele(AlleleKind.SHORT_FUR), Allele(AlleleKind.SHORT_FUR))))
    }
  }
}
