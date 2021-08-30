package model

import model.BunnyUtils.{getCouples, getStandardBunny}
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {
  "Each StandardBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy getStandardBunny()
  }

  it should "have all kind of Genes" in {
    assert(getStandardBunny().genotype.genes.size == GeneKind.values.size)
  }

  it should "have a Phenotype with only base attributes" in {
    getStandardBunny().genotype.getPhenotype().attributes.foreach(entry =>
      assert(entry._2 == entry._1.base))
  }

  "Couples of bunnies " should "be generated from any group of Bunnies" in {
    val someBunnies = Seq.fill(9)(getStandardBunny())
    val someCouples = getCouples(someBunnies)
    assert(someCouples.size == someBunnies.size/2)
  }
}
