package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_BUNNY_AGE
import it.unibo.pps.bunny.model.Bunny.{ generateRandomFirstBunny, splitBunniesByGene }
import it.unibo.pps.bunny.model.genome.{ Gene, Genes, StandardAllele }
import it.unibo.pps.bunny.model.world.Reproduction.{
  combineCouples, generateAllChildren, generateChildren, nextGenerationBunnies
}
import org.scalatest.{ FlatSpec, Matchers }

class TestReproduction extends FlatSpec with Matchers {

  "Couples of bunnies " should "be generable from any group of Bunnies" in {
    val someBunnies = Seq.fill(9)(generateRandomFirstBunny)
    val someCouples = combineCouples(someBunnies)
    assert(someCouples.size == someBunnies.size / 2)
  }

  they should "contain every Bunny in the original group, if they are even" in {
    val someBunnies = Seq.fill(6)(generateRandomFirstBunny)
    val bunniesInCouples = combineCouples(someBunnies).flatMap(couple => List(couple._1, couple._2))
    someBunnies.foreach(b => assert(bunniesInCouples.contains(b)))
  }

  they should "contain every Bunny in the original group except from one, if they are odd" in {
    val someBunnies = Seq.fill(11)(generateRandomFirstBunny)
    val bunniesInCouples = combineCouples(someBunnies).flatMap(couple => List(couple._1, couple._2))
    assert(someBunnies.count(b => !bunniesInCouples.contains(b)) == 1)
    someBunnies.filter(b => bunniesInCouples.contains(b)).foreach(b => assert(bunniesInCouples.contains(b)))
  }

  they should "be empty, if there was only one Bunny" in {
    val oneBunny = Seq(generateRandomFirstBunny)
    val bunniesInCouples = combineCouples(oneBunny)
    assert(bunniesInCouples.isEmpty)
  }

  val mom: FirstBunny = generateRandomFirstBunny
  val dad: FirstBunny = generateRandomFirstBunny
  val children: Seq[Bunny] = generateChildren(mom, dad)

  "Children of a couple" should "be 4" in {
    assert(children.size == 4)
  }

  they should "all have age 0" in {
    assert(children.count(_.age != 0) == 0)
  }

  they should "have the original bunnies as mom and dad " in {
    children.foreach(child => assert(child.mom.get == mom && child.dad.get == dad))
  }

  they should "be one for each cell of the Punnett square, for each Gene" in {
    Genes.values.foreach(gk => {
      val grandmaMomAllele = mom.genotype(gk).momAllele.kind
      val grandpaMomAllele = mom.genotype(gk).dadAllele.kind
      val grandmaDadAllele = dad.genotype(gk).momAllele.kind
      val grandpaDadAllele = dad.genotype(gk).dadAllele.kind
      val childrenGenesOfType = children.map(b => b.genotype(gk))

      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandpaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandpaDadAllele))))
    })
  }

  val bunniesNum = 7
  val bunnies: Seq[Bunny] = List.fill(bunniesNum)(generateRandomFirstBunny)

  "Children of all bunnies" should "be 4 for each couple" in {
    val children = generateAllChildren(bunnies)
    assert(children.size == (bunniesNum / 2) * 4)
  }

  they should "be zero if there were no couples and just one Bunny" in {
    val oneBunny = Seq(generateRandomFirstBunny)
    val children = generateAllChildren(oneBunny)
    assert(children.size == 0)
  }

  val nextGenBunnies: Seq[Bunny] = nextGenerationBunnies(bunnies)

  "Next generation " should "contain 4 children for each couple and the previous bunnies" in {
    assert(nextGenBunnies.size == (bunniesNum / 2) * 4 + bunniesNum)
  }

  it should "contain just one bunny if there was only one" in {
    assert(nextGenerationBunnies(Seq(generateRandomFirstBunny)).size == 1)
  }

//  it should "contain all of the original bunnies in the next generation" in {
//    assert(bunnies.toSet.subsetOf(nextGenBunnies.toSet))
//  }

  it should "not contain any of the original bunnies after MAX_AGE generations" in {
    var nextGen = bunnies
    for (_ <- 0 to MAX_BUNNY_AGE) {
      nextGen = nextGenerationBunnies(nextGen)
    }
    bunnies.foreach(b => assert(!nextGen.contains(b)))
  }

  var genBunnies: Seq[Bunny] = List.fill(bunniesNum)(generateRandomFirstBunny)

  it should "contain the right number of bunnies after many generations and they should all be alive " in {
    val generations = 8
    var num = genBunnies.size
    var oldBunnies = 0
    for (_ <- 0 to generations) {
      oldBunnies = genBunnies.count(b => b.age == MAX_BUNNY_AGE - 1)
      genBunnies = nextGenerationBunnies(genBunnies)
      assert(genBunnies.size == (num / 2) * 4 + num - oldBunnies)
      assert(genBunnies.count(_.alive) == (num / 2) * 4 + num - oldBunnies)
      num = genBunnies.size
    }
  }

  "Bunnies " should "be splittable by gene" in {
    val bunnies: List[Bunny] = List.fill(10)(generateRandomFirstBunny)
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
    noException should be thrownBy List.fill(totBunnies)(generateRandomFirstBunny)
  }

}
