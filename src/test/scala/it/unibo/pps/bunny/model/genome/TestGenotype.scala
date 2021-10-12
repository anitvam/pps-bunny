package it.unibo.pps.bunny.model.genome

import it.unibo.pps.bunny.model.{IllegalGenotypeBuildException, InconsistentGenotypeException}
import it.unibo.pps.bunny.model.bunny.Bunny.{baseBunnyGenerator, randomGenderChooser}
import it.unibo.pps.bunny.model.bunny.FirstBunny
import it.unibo.pps.bunny.model.genome.Genes.{EARS, FUR_COLOR, FUR_LENGTH, GeneKind}
import org.scalatest.{FlatSpec, Matchers}

class TestGenotype extends FlatSpec with Matchers{

  "Any Genotype" should "throw an Exception if the GeneKind in the key is not consistent with the kind in the corresponding Gene" in {
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

  "Any completed Genotype" should "throw an Exception if does not contain all kind of Genes" in {
    assertThrows[IllegalGenotypeBuildException] {
      CompleteGenotype(
        Map(
          FUR_COLOR -> Gene(Genes.FUR_COLOR, StandardAllele(Alleles.WHITE_FUR), StandardAllele(Alleles.BROWN_FUR)),
          FUR_LENGTH -> Gene(Genes.FUR_LENGTH, StandardAllele(Alleles.SHORT_FUR), StandardAllele(Alleles.SHORT_FUR))
        )
      )
    }
  }

  it should "be editable adding any Gene, which will replace the previous one with the same GeneKind" in {
    val geneKind = Genes.FUR_COLOR
    val standardGene = Gene(geneKind, StandardAllele(geneKind.base), StandardAllele(geneKind.base))
    val updatedGene = Gene(geneKind, StandardAllele(geneKind.base), StandardAllele(geneKind.mutated))
    val standardBunny = baseBunnyGenerator(randomGenderChooser())
    val updatedBunny = new FirstBunny(standardBunny.genotype + updatedGene, standardBunny.gender)

    println(standardBunny.genotype(geneKind))
    println(standardGene)
    assert(standardBunny.genotype(geneKind) == standardGene)
    assert(updatedBunny.genotype(geneKind) == updatedGene)
  }

}
