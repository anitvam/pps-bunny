package it.unibo.pps.bunny.model.genome

import it.unibo.pps.bunny.model.MultipleDominanceAssignmentException
import it.unibo.pps.bunny.model.genome.KindsUtils.{assignRandomDominance, isDominanceAssigned, resetDominance, setAlleleDominance}
import org.scalatest.{FlatSpec, Matchers}

class TestKinds extends FlatSpec with Matchers {

  "Each AlleleKind" should "be the mutated or base Allele of exactly one GeneKind" in {
    Alleles.values.foreach(ak => {
      assert(Genes.values.flatMap(gk => List(gk.base, gk.mutated)).count(_ == ak) == 1)
    })
  }

  it should "be settable as dominant just once" in {
    resetDominance()
    Genes.values.foreach(gk => {
      setAlleleDominance(gk.base)
      assertThrows[MultipleDominanceAssignmentException] {
        setAlleleDominance(gk.base)
      }
    })

    resetDominance()
    Genes.values.foreach(gk => {
      setAlleleDominance(gk.mutated)
      assertThrows[MultipleDominanceAssignmentException] {
        setAlleleDominance(gk.mutated)
      }
    })
  }

  it should "not be possible to set the dominance of the base AlleleKind of a GeneKind " +
    "if the dominance is already set for the mutated AlleleKind of the same GeneKind (and viceversa)" in {
      resetDominance()
      Genes.values.foreach(gk => {
        setAlleleDominance(gk.mutated)
        assertThrows[MultipleDominanceAssignmentException] {
          setAlleleDominance(gk.base)
        }
      })
    }

  it should "be also viceversa" in {
    resetDominance()
    Genes.values.foreach(gk => {
      setAlleleDominance(gk.base)
      assertThrows[MultipleDominanceAssignmentException] {
        setAlleleDominance(gk.mutated)
      }
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    Genes.values.foreach(gk => assert(gk.base != gk.mutated))
  }

  it should "be possible to know it its dominance has been assigned" in {
    resetDominance()
    Genes.values.foreach(isDominanceAssigned(_) == false)
    assignRandomDominance()
    Genes.values.foreach(isDominanceAssigned(_) == true)

    resetDominance()
    setAlleleDominance(Alleles.BROWN_FUR)
    isDominanceAssigned(Genes.FUR_COLOR)
    Genes.values.filter(_ != Genes.FUR_COLOR).foreach(isDominanceAssigned(_) == false)
  }

}
