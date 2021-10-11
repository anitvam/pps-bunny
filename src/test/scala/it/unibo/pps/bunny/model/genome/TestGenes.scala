package it.unibo.pps.bunny.model.genome

import it.unibo.pps.bunny.model._
import it.unibo.pps.bunny.model.genome.KindsUtils.{assignRandomDominance, getGeneKind, resetDominance}
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

}
