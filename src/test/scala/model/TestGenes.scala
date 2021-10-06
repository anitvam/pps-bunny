package model

import model.Bunny.generateBaseFirstBunny
import model.genome.Genes.{EARS, FUR_COLOR, FUR_LENGTH, GeneKind}
import model.genome.KindsUtils.{assignRandomDominance, getGeneKind, resetDominance}
import model.genome._
import org.scalatest.{FlatSpec, Matchers}

class TestGenes extends FlatSpec with Matchers {

  "Any Allele" should "not produce letters if the dominance is not defined yet" in {
    resetDominance()
    Alleles.values.foreach(ak => {
      val allele = StandardAllele(ak)
      assert(allele.getLetter == "")
    })
  }

  it should "be settable as dominant" in {
    resetDominance()
    noException should be thrownBy assignRandomDominance()
  }

  it should "produce an uppercase letter if dominant" in {
    Alleles.values
      .filter(_.isDominant.get)
      .foreach(ak => {
        assert(StandardAllele(ak).getLetter.toCharArray()(0).isUpper)
      })
  }

  it should "produce a lowercase letter if recessive" in {
    Alleles.values
      .filter(!_.isDominant.get)
      .foreach(ak => {
        assert(StandardAllele(ak).getLetter.toCharArray()(0).isLower)
      })
  }

  it should "throw and Exception if is set as mutated but the kind is the base one" in {
    Alleles.values
      .filter(ak => getGeneKind(ak).base == ak)
      .foreach(ak => {
        assertThrows[InconsistentMutatedAlleleException] {
          JustMutatedAllele(ak)
        }
      })
  }

  "Any Gene" should "throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[InconsistentAlleleException] {
      Gene(Genes.FUR_COLOR, StandardAllele(Alleles.LONG_FUR), StandardAllele(Alleles.BROWN_FUR))
    }
  }

  it should "be initialized with Alleles of the right kind" in {
    noException should be thrownBy Gene(
      Genes.FUR_COLOR,
      StandardAllele(Alleles.WHITE_FUR),
      StandardAllele(Alleles.WHITE_FUR)
    )
  }

  it should "be inferable from any of its Alleles" in {
    Genes.values.foreach(gk => {
      assert(getGeneKind(gk.base) == gk)
      assert(getGeneKind(gk.mutated) == gk)
    })
  }

  "Any Genotype" should "throw an Exception if the GeneType in the key is not coherent with the kind in the corresponding Gene" in {
    assertThrows[InconsistentGenotypeException] {
      val inconsistentGenes: Map[GeneKind, Gene] = Map(
        FUR_COLOR -> Gene(Genes.EARS, StandardAllele(Alleles.HIGH_EARS), StandardAllele(Alleles.HIGH_EARS)),
        FUR_LENGTH -> Gene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.SHORT_FUR))
      )
      PartialGenotype(inconsistentGenes)
    }
  }

  it should "know if it contains any mutated allele" in {
    val standardGenes: Map[GeneKind, Gene] = Map(
      EARS -> Gene(Genes.EARS, StandardAllele(Alleles.HIGH_EARS), StandardAllele(Alleles.HIGH_EARS)),
      FUR_LENGTH -> Gene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.LONG_FUR))
    )
    assert(!PartialGenotype(standardGenes).isJustMutated)

    val mutatedGenes: Map[GeneKind, Gene] = Map(
      EARS -> Gene(Genes.EARS, StandardAllele(Alleles.HIGH_EARS), StandardAllele(Alleles.HIGH_EARS)),
      FUR_LENGTH -> Gene(Genes.FUR_LENGTH, JustMutatedAllele(Alleles.LONG_FUR), StandardAllele(Alleles.SHORT_FUR))
    )
    assert(PartialGenotype(mutatedGenes).isJustMutated)
  }

  "Any completed Genotype" should "throw an Exception if does not contain kind of Genes" in {
    assertThrows[IllegalGenotypeBuildException] {
      CompleteGenotype(
        Map(
          FUR_COLOR -> Gene(Genes.FUR_COLOR, StandardAllele(Alleles.WHITE_FUR), StandardAllele(Alleles.BROWN_FUR)),
          FUR_LENGTH -> Gene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.SHORT_FUR))
        )
      )
    }
  }

  it should "be editable adding any Gene, which will replace the previous one with the same GeneType" in {
    val geneKind = Genes.FUR_COLOR
    val standardGene = Gene(geneKind, StandardAllele(geneKind.base), StandardAllele(geneKind.base))
    val updatedGene = Gene(geneKind, StandardAllele(geneKind.base), StandardAllele(geneKind.mutated))
    val standardBunny = generateBaseFirstBunny
    val updatedBunny = new FirstBunny(standardBunny.genotype + updatedGene)

    println(standardBunny.genotype(geneKind))
    println(standardGene)
    assert(standardBunny.genotype(geneKind) == standardGene)
    assert(updatedBunny.genotype(geneKind) == updatedGene)
  }

}
