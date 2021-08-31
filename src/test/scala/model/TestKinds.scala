package model

import org.scalatest.{FlatSpec, Matchers}

class TestKinds extends FlatSpec with Matchers {
  "Each AlleleKind" should "be in max one GeneKind" in {
    Alleles.values.foreach(anyAlleleKind => {
      assert(Genes.values.flatMap(gk => List(gk.base, gk.mutated)).count(_ == anyAlleleKind) <= 1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    Genes.values.foreach(gk => assert(gk.base != gk.mutated))
  }
}
