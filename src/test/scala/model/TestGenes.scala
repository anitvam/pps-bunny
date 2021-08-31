package model

import model.BunnyUtils.getStandardBunny
import model.Genes.{FUR_COLOR, FUR_LENGTH}
import model.GenesUtils.{assignRandomDominance, getGeneKind}
import org.scalatest.{FlatSpec, Matchers}

class TestGenes extends FlatSpec with Matchers {
  "Each AlleleKind" should "be in max one GeneKind" in {
    Alleles.values.foreach(anyAlleleKind => {
      assert(Genes.values.flatMap(gk => List(gk.base, gk.mutated)).count(_ == anyAlleleKind) <= 1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    Genes.values.foreach(gk => assert(gk.base != gk.mutated))
  }

  "Any Allele" should "not produce letters if the dominance is not defined yet" in {
    Alleles.values.foreach(ak => {
      val geneKind = getGeneKind(ak)
      val dominantAllele = Allele(geneKind.base)
      assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter) == "")
    })
  }

  it should "be settable as dominant" in{
    noException should be thrownBy assignRandomDominance()
  }

  it should "produce an uppercase letter if dominant" in {
    Alleles.values.filter(_.isDominant.get).foreach(ak => {
      assert(Allele(ak).getCaseSensitiveLetter(getGeneKind(ak).letter).toCharArray()(0).isUpper)
    })
  }

  it should "produce a lowercase letter if recessive" in {
    Alleles.values.filter(!_.isDominant.get).foreach(ak => {
      assert(Allele(ak).getCaseSensitiveLetter(getGeneKind(ak).letter).toCharArray()(0).isLower)
    })
  }

  "Any Gene" should "throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[IllegalAlleleArgumentException] {
      Gene(Genes.FUR_COLOR, Allele(Alleles.LONG_FUR), Allele(Alleles.BROWN_FUR))
    }
  }

  it should " be initialized with Alleles of the right kind" in {
    noException should be thrownBy Gene(Genes.FUR_COLOR, Allele(Alleles.WHITE_FUR), Allele(Alleles.WHITE_FUR))
  }

  it should " be inferable from any of its Alleles" in {
    Genes.values.foreach(genekind => {
      assert(getGeneKind(genekind.base) == genekind)
      assert(getGeneKind(genekind.mutated) == genekind)
    })
  }

  "Any Genotype" should "throw an Exception if the GeneType in the key is not coherent with the kind in the corresponding Gene" in {
    assertThrows[GenotypeInconsistencyException] {
      Genotype(Map( FUR_COLOR -> Gene(Genes.EARS, Allele(Alleles.WHITE_FUR), Allele(Alleles.BROWN_FUR)),
                    FUR_LENGTH -> Gene(Genes.FUR_LENGTH, Allele(Alleles.SHORT_FUR), Allele(Alleles.SHORT_FUR))))
    }
  }

  it should "be editable adding any Gene, which will replace the previous one with the same GeneType" in {
    val geneKind = Genes.FUR_COLOR
    val standardGene =  Gene(geneKind, Allele(geneKind.base),  Allele(geneKind.base))
    val updatedGene =  Gene(geneKind, Allele(geneKind.base),  Allele(geneKind.mutated))
    val standardBunny = getStandardBunny
    val updatedBunny = Bunny(Genotype(standardBunny.genotype + updatedGene))

    assert(standardBunny.genotype.genes(geneKind) == standardGene)
    assert(updatedBunny.genotype.genes(geneKind) == updatedGene)
  }
}
