package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.engine.SimulationConstants._
import it.unibo.pps.bunny.model.bunny.Bunny._
import it.unibo.pps.bunny.model.bunny.{ Bunny, Female, Male }
import it.unibo.pps.bunny.model.genome.{ Gene, Genes, StandardAllele }
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.Reproduction._
import org.scalatest.{ FlatSpec, Matchers }

class TestReproduction extends FlatSpec with Matchers {

  "A couple" should "have one Male and one Female Bunny" in {
    assertThrows[CoupleGendersException] {
      Couple(baseBunnyGenerator(Male), baseBunnyGenerator(Male))
    }
    assertThrows[CoupleGendersException] {
      Couple(baseBunnyGenerator(Female), baseBunnyGenerator(Female))
    }
  }

  "Couples of bunnies " should "be generabile from any group of bunnies with males and females" in {
    val someBunnies = Seq.fill(9)(randomBunnyGenerator())
    val someCouples = combineCouples(someBunnies)
    if (someBunnies.count(_.gender == Male) > 0 && someBunnies.count(_.gender == Female) > 0) {
      assert(someCouples.nonEmpty)
    } else {
      assert(someCouples.isEmpty)
    }
  }

  they should "contain all the possible couples" in {
    val someBunnies = Seq.fill(10)(randomBunnyGenerator())
    val someCouples = combineCouples(someBunnies)
    assert(someCouples.size == math.min(someBunnies.count(_.gender == Male), someBunnies.count(_.gender == Female)))
  }

  they should "only contain bunnies of the original group" in {
    val someBunnies = Seq.fill(11)(randomBunnyGenerator())
    val bunniesInCouples = combineCouples(someBunnies).flatMap(_.toSeq)
    bunniesInCouples.foreach(b => assert(someBunnies.contains(b)))
  }

  they should "be empty, if there was only one bunny" in {
    val oneBunny = Seq(randomBunnyGenerator())
    val bunniesInCouples = combineCouples(oneBunny)
    assert(bunniesInCouples.isEmpty)
  }

  they should "be empty, if there was bunnies of only one gender" in {
    val gender = randomGenderChooser()
    val sameGenderBunnies = Seq.fill(5)(baseBunnyGenerator(gender))
    val bunniesInCouples = combineCouples(sameGenderBunnies)
    assert(bunniesInCouples.isEmpty)
  }

  private val couple: Couple = initialCoupleGenerator()
  private val children: Seq[Bunny] = generateChildren(couple)

  "Children of a couple" should "be in the right amount" in {
    assert(children.size == CHILDREN_FOR_EACH_COUPLE)
  }

  they should "be 2 males and 2 females" in {
    assert(children.count(_.gender == Male) == CHILDREN_FOR_EACH_COUPLE / 2)
    assert(children.count(_.gender == Female) == CHILDREN_FOR_EACH_COUPLE / 2)
  }

  they should "all have age 0" in {
    assert(children.count(_.age != 0) == 0)
  }

  they should "have the original bunnies as mom and dad " in {
    children.foreach(child => assert(child.mom.get == couple.mom && child.dad.get == couple.dad))
  }

  they should "be one for each cell of the Punnett square, for each Gene" in {
    Genes.values.foreach(gk => {
      val grandmaMomAllele = couple.mom.genotype(gk).momAllele.kind
      val grandpaMomAllele = couple.mom.genotype(gk).dadAllele.kind
      val grandmaDadAllele = couple.dad.genotype(gk).momAllele.kind
      val grandpaDadAllele = couple.dad.genotype(gk).dadAllele.kind
      val childrenGenesOfType = children.map(b => b.genotype(gk))

      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandpaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandpaDadAllele))))
    })
  }

  private val bunniesNum = 20
  private val bunnies: Seq[Bunny] = Seq.fill(bunniesNum)(randomBunnyGenerator())
  private val couplesNum: Int = combineCouples(bunnies).size

  "Children of all bunnies" should "be 4 for each couple" in {
    val children = generateAllChildren(bunnies)
    assert(children.size == couplesNum * 4)
  }

  they should "be zero if there were no couples and just one bunny" in {
    val oneBunny = Seq(randomBunnyGenerator())
    val children = generateAllChildren(oneBunny)
    assert(children.isEmpty)
  }

  private val nextGenBunnies: Seq[Bunny] = nextGenerationBunnies(bunnies)

  "Second generation" should "contain 4 children for each couple and the previous bunnies" in {
    assert(nextGenBunnies.size == couplesNum * 4 + bunniesNum)
  }

  it should "contain just one bunny if there was only one" in {
    assert(nextGenerationBunnies(Seq(randomBunnyGenerator())).size == 1)
  }

  it should "contain just the original bunnies if they were all of the same gender" in {
    assert(nextGenerationBunnies(List.fill(10)(baseBunnyGenerator(Male))).size == 10)
    assert(nextGenerationBunnies(List.fill(10)(baseBunnyGenerator(Female))).size == 10)
  }

  it should "not contain any of the original bunnies after MAX_AGE generations" in {
    var nextGen = bunnies
    for (_ <- 0 to MAX_BUNNY_AGE) {
      nextGen = nextGenerationBunnies(nextGen)
    }
    bunnies.foreach(b => assert(!nextGen.contains(b)))
  }

  private var genBunnies: Seq[Bunny] = List.fill(bunniesNum)(randomBunnyGenerator())

  "Next generation" should "contain the right number of bunnies after many generations and they should all be alive" in {
    val generations = 8
    var num = genBunnies.size
    val getCouplesNum: Population => Int = bunnies => combineCouples(bunnies).size
    var oldBunnies = 0
    for (_ <- 0 to generations) {
      oldBunnies = genBunnies.count(b => b.age == MAX_BUNNY_AGE - 1)
      val couples = getCouplesNum(genBunnies)
      val expectedNum = couples * 4 + num - oldBunnies
      genBunnies = nextGenerationBunnies(genBunnies)
      assert(genBunnies.size == expectedNum)
      assert(genBunnies.count(_.alive) == expectedNum)
      num = genBunnies.size
    }
  }

}
