package model.world.disturbingFactors

import engine.SimulationConstants.{ FOOD_FACTOR_LOW_DAMAGE, FOOD_FACTOR_NORMAL_DAMAGE }
import model.{ Bunny, InvalidFoodFactor }
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.Climate
import model.world.Generation.Population
import model.world.disturbingFactors.BasicFactor
import model.world.disturbingFactors.FactorTypes._
import model.world.disturbingFactors.FactorsUtils.{ applyCustomDamage, filterBunniesWithAlleles }

sealed trait FoodFactor extends Factor {

  override val normalDamage: Double = FOOD_FACTOR_NORMAL_DAMAGE
  override def factorType: FactorKind = FoodFactorKind

  val lowDamage: Double = FOOD_FACTOR_LOW_DAMAGE

  def combineWith(foodFactor: FoodFactor): FoodFactor
  def isCombined: Boolean
  def removeSubFactor(foodFactor: FoodFactor): FoodFactor
}

abstract class SingleFoodFactor(override val isCombined: Boolean = false) extends BasicFactor with FoodFactor {
  val concatFactor: PartialFunction[FoodFactor, FoodFactor]

  override def combineWith(foodFactor: FoodFactor): FoodFactor =
    concatFactor applyOrElse (foodFactor, throw new InvalidFoodFactor())

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

case class LimitedFoodFactor(override val isCombined: Boolean = false) extends SingleFoodFactor {

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: HighFoodFactor  => LimitedHighFoodFactor()
    case _: ToughFoodFactor => LimitedToughFoodFactor()
  }

}

case class HighFoodFactor(override val affectedGene: GeneKind = Genes.JUMP)
    extends SingleFoodFactor
    with FactorWithOneGene {

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(affectedGene, bunnies)._1, climate)
    bunnies
  }

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => LimitedHighFoodFactor()
    case _: ToughFoodFactor   => LimitedToughFoodFactor()
  }

}

case class ToughFoodFactor(override val affectedGene: GeneKind = Genes.TEETH)
    extends SingleFoodFactor
    with FactorWithOneGene {

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(affectedGene, bunnies)._1, climate)
    bunnies
  }

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => LimitedToughFoodFactor()
    case _: HighFoodFactor    => HighToughFoodFactor()
  }

}

case class LimitedHighFoodFactor(
    override val affectedGene: GeneKind = Genes.JUMP,
    override val isCombined: Boolean = true
) extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: ToughFoodFactor => LimitedHighToughFoodFactor()
    case _                  => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(affectedGene, bunnies)
    super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
    applyCustomDamage(bunniesSplitByHighFoodGene._2, lowDamage)
    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => HighFoodFactor()
    case _: HighFoodFactor    => LimitedFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

}

case class LimitedToughFoodFactor(
    override val affectedGene: GeneKind = Genes.TEETH,
    override val isCombined: Boolean = true
) extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: HighFoodFactor => LimitedHighToughFoodFactor()
    case _                 => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByToughFoodGene = Bunny.splitBunniesByGene(affectedGene, bunnies)

    super.applyDamage(bunniesSplitByToughFoodGene._1, climate)
    applyCustomDamage(bunniesSplitByToughFoodGene._2, lowDamage)

    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => ToughFoodFactor()
    case _: ToughFoodFactor   => LimitedFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

}

case class HighToughFoodFactor(
    override val isCombined: Boolean = true,
    override val firstGeneAffected: GeneKind = Genes.JUMP,
    override val secondGeneAffected: GeneKind = Genes.TEETH
) extends BasicFactor
    with FactorWithTwoGenes
    with FoodFactor {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => LimitedHighToughFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(firstGeneAffected, bunnies)
    super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
      lowDamage
    )
    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: HighFoodFactor  => ToughFoodFactor()
    case _: ToughFoodFactor => HighFoodFactor()
    case _                  => throw new InvalidFoodFactor()
  }

}

case class LimitedHighToughFoodFactor(
    override val isCombined: Boolean = true,
    override val firstGeneAffected: GeneKind = Genes.JUMP,
    override val secondGeneAffected: GeneKind = Genes.TEETH
) extends BasicFactor
    with FactorWithTwoGenes
    with FoodFactor {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = throw new InvalidFoodFactor()

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(firstGeneAffected, bunnies)
    super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
    super.applyDamage(
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
      climate
    )
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.mutated),
      lowDamage
    )
    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => HighToughFoodFactor()
    case _: ToughFoodFactor   => LimitedHighFoodFactor()
    case _: HighFoodFactor    => LimitedToughFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

}
