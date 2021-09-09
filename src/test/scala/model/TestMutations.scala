package model

import model.genome.{Alleles, Genes}
import model.mutation.Mutations
import org.scalatest.{FlatSpec, Matchers}

class TestMutations extends FlatSpec with Matchers {
  "Each Mutation" should "have the counterpart DOMINANT and RECESSIVE for every GeneKind" in {
    Mutations.values.foreach(ak => {
      assert(Mutations.values.size == 2 * Genes.values.size)
    })
  }
}
