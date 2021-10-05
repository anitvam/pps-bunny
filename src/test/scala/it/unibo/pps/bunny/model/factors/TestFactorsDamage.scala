package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.engine.SimulationConstants.FactorsConstants._
import it.unibo.pps.bunny.model.Bunny
import it.unibo.pps.bunny.model.Bunny.{ filterBunniesWithAlleles, generateRandomFirstBunny, splitBunniesByGene }
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.{ Alleles, Genes }
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.{
  HighFoodFactor, HighToughFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor, LimitedHighToughFoodFactor,
  LimitedToughFoodFactor, ToughFoodFactor, _
}
import it.unibo.pps.bunny.model.world.{ Summer, Winter }
import org.scalatest.{ FlatSpec, Matchers }

class TestFactorsDamage extends FlatSpec with Matchers {

  "A population of bunnies" should "be filtered by alleles" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val firstGene: GeneKind = Genes.FUR_COLOR
    val secondGene: GeneKind = Genes.EARS
    val bunniesWithNormalDamage = filterBunniesWithAlleles(bunnies, firstGene.base, secondGene.base)
    val bunniesWithHighDamage = filterBunniesWithAlleles(bunnies, firstGene.mutated, secondGene.mutated)
    val others = bunnies diff bunniesWithNormalDamage diff bunniesWithHighDamage

    assert(bunniesWithHighDamage.length + bunniesWithNormalDamage.length + others.length == bunnies.size)
  }

  "UnfriendlyClimate Factor" should "kill 20% of bunnies with Long Fur on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val unfriendlyClimateFactor = UnfriendlyClimateFactor()
    val splitBunnies = splitBunniesByGene(Genes.FUR_LENGTH, bunnies)

    unfriendlyClimateFactor.applyDamage(bunnies, Summer())
    assert(
      bunnies.count(!_.alive) == (splitBunnies._2.length * UNFRIENDLY_CLIMATE_DAMAGE).round.toInt
    )
  }

  it should "kill 20% of bunnies with ShortFur on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = UnfriendlyClimateFactor()
    val splitBunnies = splitBunniesByGene(Genes.FUR_LENGTH, bunnies)

    factor.applyDamage(bunnies, Winter())
    assert(
      bunnies.count(!_.alive) == (splitBunnies._1.length * UNFRIENDLY_CLIMATE_DAMAGE).round.toInt
    )
  }

  "Wolf Factor" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = WolvesFactor()
    val lowAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.BROWN_FUR, Alleles.HIGH_EARS)
    val lowAffectedNumber = (lowAffectedBunnies.length * WOLF_LOW_DAMAGE).round.toInt
    val highAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.WHITE_FUR, Alleles.LOW_EARS)
    val highAffectedNumber = (highAffectedBunnies.length * WOLF_HIGH_DAMAGE).round.toInt
    val otherAffected =
      ((bunnies.length - lowAffectedBunnies.length - highAffectedBunnies.length) * WOLF_MEDIUM_DAMAGE).round.toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == lowAffectedNumber + highAffectedNumber + otherAffected)
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = WolvesFactor()
    val lowAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.WHITE_FUR, Alleles.HIGH_EARS)
    val lowAffectedNumber = (lowAffectedBunnies.length * WOLF_LOW_DAMAGE).round.toInt
    val highAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.BROWN_FUR, Alleles.LOW_EARS)
    val highAffectedNumber = (highAffectedBunnies.length * WOLF_HIGH_DAMAGE).round.toInt
    val otherAffected =
      ((bunnies.length - lowAffectedBunnies.length - highAffectedBunnies.length) * WOLF_MEDIUM_DAMAGE).round.toInt

    factor.applyDamage(bunnies, Winter())
    assert(bunnies.count(!_.alive) == lowAffectedNumber + highAffectedNumber + otherAffected)
  }

  "HighFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = HighFoodFactor()

    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)._1

    assert(
      factor
        .applyDamage(bunnies, Winter())
        .count(!_.alive) == (affectedBunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt
    )
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = HighFoodFactor()

    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)._1

    assert(
      factor
        .applyDamage(bunnies, Summer())
        .count(!_.alive) == (affectedBunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt
    )
  }

  "LimitedFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedFoodFactor()

    assert(
      factor.applyDamage(bunnies, Winter()).count(!_.alive) == (bunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt
    )
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedFoodFactor()

    val affectedNumber = (bunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  "ToughFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = ToughFoodFactor()

    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)._1
    val affectedNumber = (affectedBunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt

    factor.applyDamage(bunnies, Winter())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = ToughFoodFactor()

    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)._1
    val affectedNumber = (affectedBunnies.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  private def countHighLimitedFoodDamage(bunnies: Population): Int = {
    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)

    (affectedBunnies._1.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt +
      (affectedBunnies._2.length * FOOD_FACTOR_LOW_DAMAGE).round.toInt
  }

  "HighFood and LimitedFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedHighFoodFactor()

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighLimitedFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedHighFoodFactor()

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighLimitedFoodDamage(bunnies))
  }

  private def countHighToughFoodDamage(bunnies: Population): Int = {
    val bunniesLowJump = splitBunniesByGene(Genes.JUMP, bunnies)._1
    val bunniesHighJumpNormalTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.SHORT_TEETH)

    (bunniesLowJump.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt +
      (bunniesHighJumpNormalTeeth.length * FOOD_FACTOR_LOW_DAMAGE).round.toInt
  }

  "HighFood and ToughFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = HighToughFoodFactor()

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = HighToughFoodFactor()

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countHighToughFoodDamage(bunnies))
  }

  private def countLimitedToughFoodDamage(bunnies: Population): Int = {
    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)

    (affectedBunnies._1.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt +
      (affectedBunnies._2.length * FOOD_FACTOR_LOW_DAMAGE).round.toInt
  }

  "ToughFood and LimitedFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedToughFoodFactor()

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countLimitedToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedToughFoodFactor()

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countLimitedToughFoodDamage(bunnies))
  }

  private def countLimitedHighToughFoodDamage(bunnies: Population): Int = {
    val bunniesLowJump = splitBunniesByGene(Genes.JUMP, bunnies)._1
    val bunniesHighJumpNormalTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.SHORT_TEETH)
    val bunniesHighJumpLongTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.LONG_TEETH)

    (bunniesLowJump.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt +
      (bunniesHighJumpNormalTeeth.length * FOOD_FACTOR_NORMAL_DAMAGE).round.toInt +
      (bunniesHighJumpLongTeeth.length * FOOD_FACTOR_LOW_DAMAGE).round.toInt
  }

  "LimitedFood, HighFood and ToughFood" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedHighToughFoodFactor()

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countLimitedHighToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = LimitedHighToughFoodFactor()

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countLimitedHighToughFoodDamage(bunnies))
  }

}
