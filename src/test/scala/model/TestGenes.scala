package model

import model.BunnyUtils.generateBaseFirstBunny
import model.genome.Genes.{FUR_COLOR, FUR_LENGTH, GeneKind}
import model.genome.GenesUtils.{assignRandomDominance, getGeneKind}
import model.genome.{Alleles, CompletedGenotype, Gene, Genes, JustMutatedAllele, PartialGenotype, StandardAllele, StandardGene}
import org.scalatest.{FlatSpec, Matchers}

class TestGenes extends FlatSpec with Matchers {
  "Any Allele" should "not produce letters if the dominance is not defined yet" in {
    Alleles.values.foreach(ak => {
      val geneKind = getGeneKind(ak)
      val dominantAllele = StandardAllele(geneKind.base)
      assert(dominantAllele.getCaseSensitiveLetter(geneKind.letter) == "")
    })
  }

  it should "be settable as dominant" in{
    noException should be thrownBy assignRandomDominance()
  }

  it should "produce an uppercase letter if dominant" in {
    Alleles.values.filter(_.isDominant.get).foreach(ak => {
      assert(StandardAllele(ak).getCaseSensitiveLetter(getGeneKind(ak).letter).toCharArray()(0).isUpper)
    })
  }

  it should "produce a lowercase letter if recessive" in {
    Alleles.values.filter(!_.isDominant.get).foreach(ak => {
      assert(StandardAllele(ak).getCaseSensitiveLetter(getGeneKind(ak).letter).toCharArray()(0).isLower)
    })
  }

  it should "throw and Exception if is set as mutated but the kind is the base one" in {
    Alleles.values.filter(ak => getGeneKind(ak).base == ak).foreach(ak => {
      assertThrows[InconsistentMutatedAlleleException] {
        JustMutatedAllele(ak)
      }
    })
  }

  "Any Gene" should "throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[InconsistentAlleleException] {
      StandardGene(Genes.FUR_COLOR, StandardAllele(Alleles.LONG_FUR), StandardAllele(Alleles.BROWN_FUR))
    }
  }

  it should " be initialized with Alleles of the right kind" in {
    noException should be thrownBy StandardGene(Genes.FUR_COLOR, StandardAllele(Alleles.WHITE_FUR), StandardAllele(Alleles.WHITE_FUR))
  }

  it should " be inferable from any of its Alleles" in {
    Genes.values.foreach(gk => {
      assert(getGeneKind(gk.base) == gk)
      assert(getGeneKind(gk.mutated) == gk)
    })
  }

  "Any Genotype" should "throw an Exception if the GeneType in the key is not coherent with the kind in the corresponding Gene" in {
    assertThrows[InconsistentGenotypeException] {
      val genes: Map[GeneKind, Gene] = Map(
        FUR_COLOR -> StandardGene(Genes.EARS, StandardAllele(Alleles.HIGH_EARS), StandardAllele(Alleles.HIGH_EARS)),
        FUR_LENGTH -> StandardGene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.SHORT_FUR)))
      PartialGenotype(genes)
    }
  }

  "Any completed Genotype" should "throw an Exception if does not contain kind of Genes" in {
    assertThrows[IllegalGenotypeBuildException] {
      CompletedGenotype(Map(
        FUR_COLOR ->  StandardGene(Genes.FUR_COLOR, StandardAllele(Alleles.WHITE_FUR), StandardAllele(Alleles.BROWN_FUR)),
        FUR_LENGTH -> StandardGene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.SHORT_FUR))))
    }
  }

  it should "be editable adding any Gene, which will replace the previous one with the same GeneType" in {
    val geneKind = Genes.FUR_COLOR
    val standardGene =  StandardGene(geneKind, StandardAllele(geneKind.base),  StandardAllele(geneKind.base))
    val updatedGene =  StandardGene(geneKind, StandardAllele(geneKind.base),  StandardAllele(geneKind.mutated))
    val standardBunny = generateBaseFirstBunny
    val updatedBunny = new FirstBunny(standardBunny.genotype + updatedGene)

    assert(standardBunny.genotype.genes(geneKind) == standardGene)
    assert(updatedBunny.genotype.genes(geneKind) == updatedGene)
  }
}
