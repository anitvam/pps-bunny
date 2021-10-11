package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.model.HistoryBunnyUpdateException
import it.unibo.pps.bunny.model.bunny.Bunny._
import it.unibo.pps.bunny.model.genome._
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {

  "A FirstBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy baseBunnyGenerator(randomGenderChooser())
    noException should be thrownBy randomBunnyGenerator
  }

  private val baseBunny: FirstBunny = baseBunnyGenerator(randomGenderChooser())
  private val randomBunny: FirstBunny = randomBunnyGenerator()

  it should "have all kind of Genes" in {
    assert(baseBunny.genotype.genes.size == Genes.values.size)
    assert(randomBunny.genotype.genes.size == Genes.values.size)
  }

  it should "have a no parents" in {
    assert(baseBunny.mom == Option.empty)
    assert(randomBunny.mom == Option.empty)
    assert(baseBunny.dad == Option.empty)
    assert(randomBunny.dad == Option.empty)
  }

  "A Base FirstBunny" should "have a Phenotype with only base attributes" in {
    baseBunny.genotype.phenotype.visibleTraits.foreach(entry => assert(entry._2 == entry._1.base))
  }

  it should "should have a toString with the correct info" in {
    assert(baseBunny.toString.contains("gender: " + baseBunny.gender))
    assert(baseBunny.toString.contains("alive: " + baseBunny.alive))
    assert(baseBunny.toString.contains("age: " + baseBunny.age))
    assert(baseBunny.toString.contains(baseBunny.genotype.toString))
  }

  "An HistoryBunny" should "be immutable and throw an Exception when it age or alive field are modified" in {
    val historyBunny: Bunny = HistoryBunny(baseBunny)
    assertThrows[HistoryBunnyUpdateException] {
      historyBunny.kill()
    }
    assertThrows[HistoryBunnyUpdateException] {
      historyBunny.increaseAge()
    }
  }

  "Bunnies " should "be splittable by gene" in {
    val bunnies: List[Bunny] = List.fill(10)(randomBunnyGenerator())
    Genes.values.foreach(gk => {
      val baseCount = bunnies.count(_.genotype.phenotype(gk) == gk.base)
      val mutatedCount = bunnies.count(_.genotype.phenotype(gk) == gk.mutated)
      val split = splitBunniesByGene(gk, bunnies)
      assert(split._1.size == baseCount)
      assert(split._2.size == mutatedCount)
    })
  }

  it should "be possible to create a lot of them " in {
    val totBunnies = 100000
    noException should be thrownBy List.fill(totBunnies)(randomBunnyGenerator())
  }
}
