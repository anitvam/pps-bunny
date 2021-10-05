package bunny.model

import bunny.engine.SimulationConstants.MAX_BUNNY_AGE
import bunny.model.Bunny.{ generateBaseFirstBunny, generateRandomFirstBunny, splitBunniesByGene }
import bunny.model.genome.{ Gene, Genes, StandardAllele }
import bunny.model.world.Reproduction._
import org.scalatest.{ FlatSpec, Matchers }

class TestBunny extends FlatSpec with Matchers {

  "Any FirstBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy generateBaseFirstBunny
    noException should be thrownBy generateRandomFirstBunny
  }

  it should "have all kind of Genes" in {
    assert(generateBaseFirstBunny.genotype.genes.size == Genes.values.size)
    assert(generateRandomFirstBunny.genotype.genes.size == Genes.values.size)
  }

  it should "have a no parents" in {
    assert(generateBaseFirstBunny.mom == Option.empty)
    assert(generateRandomFirstBunny.mom == Option.empty)
    assert(generateBaseFirstBunny.dad == Option.empty)
    assert(generateRandomFirstBunny.dad == Option.empty)
  }

  "Any BaseFirstBunny" should "have a Phenotype with only base attributes" in {
    generateBaseFirstBunny.genotype.phenotype.visibleTraits.foreach(entry => assert(entry._2 == entry._1.base))
  }

}
