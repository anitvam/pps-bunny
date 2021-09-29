package model

import engine.SimulationConstants
import model.Bunny.{generateRandomFirstBunny, splitBunniesByGene}
import model.genome.Genes.GeneKind
import model.genome.{Alleles, Genes}
import model.world.Factor.{FoodFactorImpl, UnfriendlyClimate, Wolves}
import model.world.FactorsUtils.filterBunniesWithAlleles
import model.world.Generation.Population
import model.world.{Summer, Winter}
import org.scalatest.{FlatSpec, Matchers}

class TestFactors extends FlatSpec with Matchers {

  "A population of bunnies" should "be filtered by alleles" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val firstGene: GeneKind = Genes.FUR_COLOR
    val secondGene: GeneKind = Genes.EARS
    val bunniesWithNormalDamage = filterBunniesWithAlleles(bunnies, firstGene.base, secondGene.base)
    val bunniesWithHighDamage = filterBunniesWithAlleles(bunnies, firstGene.mutated, secondGene.mutated)
    val others = bunnies diff bunniesWithNormalDamage diff bunniesWithHighDamage

    assert( bunniesWithHighDamage.length + bunniesWithNormalDamage.length + others.length == bunnies.size )
  }

  "UnfriendlyClimate Factor" should "kill 20% of bunnies with Long Fur on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val unfriendlyClimateFactor = UnfriendlyClimate()
    val splitBunnies = splitBunniesByGene(Genes.FUR_LENGTH, bunnies)

    unfriendlyClimateFactor.applyDamage(bunnies, Summer())
    assert( bunnies.count(!_.alive) == (splitBunnies._2.length * SimulationConstants.UNFRIENDLY_CLIMATE_DAMAGE).toInt  )
  }

  it should "kill 20% of bunnies with ShortFur on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = UnfriendlyClimate()
    val splitBunnies = splitBunniesByGene(Genes.FUR_LENGTH, bunnies)

    factor.applyDamage(bunnies, Winter())
    assert( bunnies.count(!_.alive) == (splitBunnies._1.length * SimulationConstants.UNFRIENDLY_CLIMATE_DAMAGE).toInt  )
  }

  "Wolf Factor" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = Wolves()
    val lowAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.BROWN_FUR, Alleles.HIGH_EARS)
    val lowAffectedNumber = (lowAffectedBunnies.length * SimulationConstants.WOLF_LOW_DAMAGE).toInt
    val highAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.WHITE_FUR, Alleles.LOW_EARS)
    val highAffectedNumber = (highAffectedBunnies.length * SimulationConstants.WOLF_HIGH_DAMAGE).toInt
    val otherAffected = ((bunnies.length - lowAffectedBunnies.length - highAffectedBunnies.length) * SimulationConstants.WOLF_MEDIUM_DAMAGE).toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == lowAffectedNumber + highAffectedNumber + otherAffected)
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = Wolves()
    val lowAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.WHITE_FUR, Alleles.HIGH_EARS)
    val lowAffectedNumber = (lowAffectedBunnies.length * SimulationConstants.WOLF_LOW_DAMAGE).toInt
    val highAffectedBunnies = filterBunniesWithAlleles(bunnies, Alleles.BROWN_FUR, Alleles.LOW_EARS)
    val highAffectedNumber = (highAffectedBunnies.length * SimulationConstants.WOLF_HIGH_DAMAGE).toInt
    val otherAffected = ((bunnies.length - lowAffectedBunnies.length - highAffectedBunnies.length) * SimulationConstants.WOLF_MEDIUM_DAMAGE).toInt

    factor.applyDamage(bunnies, Winter())
    assert(bunnies.count(!_.alive) == lowAffectedNumber + highAffectedNumber + otherAffected)
  }

  "HighFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = false, isToughFood = false)

    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)._1

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == (affectedBunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt)
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = false, isToughFood = false)

    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)._1

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == (affectedBunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt)
  }

  "LimitedFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = true, isToughFood = false)

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == (bunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt)
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = true, isToughFood = false)

    val affectedNumber = (bunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  "ToughFood Factor" should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = false, isToughFood = true)

    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)._1
    val affectedNumber = (affectedBunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt

    factor.applyDamage(bunnies, Winter())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  it should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = false, isToughFood = true)

    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)._1
    val affectedNumber = (affectedBunnies.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt

    factor.applyDamage(bunnies, Summer())
    assert(bunnies.count(!_.alive) == affectedNumber)
  }

  private def countHighLimitedFoodDamage(bunnies: Population): Int = {
    val affectedBunnies = splitBunniesByGene(Genes.JUMP, bunnies)

    (affectedBunnies._1.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt +
      (affectedBunnies._2.length * SimulationConstants.FOOD_FACTOR_LOW_DAMAGE).toInt
  }

  "HighFood and LimitedFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = true, isToughFood = false)

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighLimitedFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = true, isToughFood = false)

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighLimitedFoodDamage(bunnies))
  }

  private def countHighToughFoodDamage(bunnies: Population): Int = {
    val bunniesLowJump = splitBunniesByGene(Genes.JUMP, bunnies)._1
    val bunniesHighJumpNormalTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.SHORT_TEETH)

    (bunniesLowJump.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt +
      (bunniesHighJumpNormalTeeth.length * SimulationConstants.FOOD_FACTOR_LOW_DAMAGE).toInt
  }

  "HighFood and ToughFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = false, isToughFood = true)

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countHighToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = false, isToughFood = true)

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countHighToughFoodDamage(bunnies))
  }

  private def countLimitedToughFoodDamage(bunnies: Population): Int = {
    val affectedBunnies = splitBunniesByGene(Genes.TEETH, bunnies)

    (affectedBunnies._1.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt +
      (affectedBunnies._2.length * SimulationConstants.FOOD_FACTOR_LOW_DAMAGE).toInt
  }

  "ToughFood and LimitedFood Factors" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = true, isToughFood = true)

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countLimitedToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = false, isLimitedFood = true, isToughFood = true)

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countLimitedToughFoodDamage(bunnies))
  }

  private def countLimitedHighToughFoodDamage(bunnies: Population): Int = {
    val bunniesLowJump = splitBunniesByGene(Genes.JUMP, bunnies)._1
    val bunniesHighJumpNormalTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.SHORT_TEETH)
    val bunniesHighJumpLongTeeth = filterBunniesWithAlleles(bunnies, Alleles.HIGH_JUMP, Alleles.LONG_TEETH)

    (bunniesLowJump.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt +
      (bunniesHighJumpNormalTeeth.length * SimulationConstants.FOOD_FACTOR_NORMAL_DAMAGE).toInt +
      (bunniesHighJumpLongTeeth.length * SimulationConstants.FOOD_FACTOR_LOW_DAMAGE).toInt
  }

  "LimitedFood, HighFood and ToughFood" should "kill some bunnies on Summer" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = true, isToughFood = true)

    assert(factor.applyDamage(bunnies, Summer()).count(!_.alive) == countLimitedHighToughFoodDamage(bunnies))
  }

  it should "kill some bunnies on Winter" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)
    val factor = FoodFactorImpl(isHighFood = true, isLimitedFood = true, isToughFood = true)

    assert(factor.applyDamage(bunnies, Winter()).count(!_.alive) == countLimitedHighToughFoodDamage(bunnies))
  }

}