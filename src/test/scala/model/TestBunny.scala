package model
import model.bunny.Bunny.{generateBaseFirstBunny, generateRandomFirstBunny}
import model.bunny.Gender._
import model.genome.Genes
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {

  "Any FirstBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy generateBaseFirstBunny(randomGender())
    noException should be thrownBy generateRandomFirstBunny
  }

  val baseBunny = generateBaseFirstBunny(randomGender())
  val randomBunny = generateRandomFirstBunny()
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

  "Any BaseFirstBunny" should "have a Phenotype with only base attributes" in {
    baseBunny.genotype.phenotype.visibleTraits.foreach(entry =>
      assert(entry._2 == entry._1.base))
  }
}
