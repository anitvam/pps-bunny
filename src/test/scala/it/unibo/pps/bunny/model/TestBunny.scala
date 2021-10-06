<<<<<<< HEAD:src/test/scala/it/unibo/pps/bunny/model/TestBunny.scala
package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_BUNNY_AGE
import it.unibo.pps.bunny.model.Bunny.{ generateBaseFirstBunny, generateRandomFirstBunny, splitBunniesByGene }
import it.unibo.pps.bunny.model.genome.{ Gene, Genes, StandardAllele }
import it.unibo.pps.bunny.model.world.Reproduction._
import org.scalatest.{ FlatSpec, Matchers }
=======
package model
import model.Bunny.{generateBaseFirstBunny, generateRandomFirstBunny}
import model.genome.Genes
import org.scalatest.{FlatSpec, Matchers}
>>>>>>> develop:src/test/scala/model/TestBunny.scala

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
