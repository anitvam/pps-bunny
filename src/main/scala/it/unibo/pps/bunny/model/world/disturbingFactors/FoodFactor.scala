package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.engine.SimulationConstants.FactorsConstants._
import it.unibo.pps.bunny.model.Bunny.filterBunniesWithAlleles
import it.unibo.pps.bunny.model.{ Bunny, InvalidFoodFactor }
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Climate
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorTypes._
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorsUtils._

sealed trait FoodFactor extends BasicFactor {
  override val normalDamage: Double = FOOD_FACTOR_NORMAL_DAMAGE
  override def factorType: FactorKind = FoodFactorKind

  /** @return the LOW DAMAGE value for this Food */
  val lowDamage: Double = FOOD_FACTOR_LOW_DAMAGE

  /** @return true if this FoodFactor is combined with another one, otherwise false */
  def isCombined: Boolean

  /**
   * Concat two FoodFactors
   * @param foodFactor
   *   the FoodFactor to be concat
   * @return
   *   the new instance of the combined FoodFactor
   * @throws InvalidFoodFactor
   *   if the specified FoodFactor could not be concat
   * @throws UnsupportedOperationException
   *   if the operation is not supported on this FoodFactor
   */
  def +(foodFactor: FoodFactor): FoodFactor

  /**
   * Split two FoodFactors
   * @param foodFactor
   *   the FoodFactor to be split
   * @return
   *   the new instance of the combined FoodFactor
   * @throws InvalidFoodFactor
   *   if the specified FoodFactor could not be split
   * @throws UnsupportedOperationException
   *   if the operation is not supported on this FoodFactor
   */
  def -(foodFactor: FoodFactor): FoodFactor
}

sealed trait FoodFactorOnSingleGene extends FactorOnSingleGene with FoodFactor {

  def applyToAllBunnies: Boolean

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val split = Bunny.splitBunniesByGene(affectedGene, bunnies)
    if (!applyToAllBunnies) super.applyDamage(split._1, climate)
    else {
      super.applyDamage(split._1, climate)
      applyCustomDamage(split._2, lowDamage)
    }
    bunnies
  }

}

abstract class SingleFoodFactor(override val isCombined: Boolean = false) extends FoodFactor {
  protected val concatFunction: PartialFunction[FoodFactor, FoodFactor]

  override def +(foodFactor: FoodFactor): FoodFactor = {
    if (concatFunction isDefinedAt foodFactor) concatFunction(foodFactor) else throw new InvalidFoodFactor()
  }

  override def -(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

abstract class DoubleFoodFactor(override val isCombined: Boolean = true) extends SingleFoodFactor {
  protected val splitFunction: PartialFunction[FoodFactor, FoodFactor]

  override def -(foodFactor: FoodFactor): FoodFactor =
    if (splitFunction isDefinedAt foodFactor) splitFunction(foodFactor) else throw new InvalidFoodFactor()

}

abstract class TripleFoodFactor() extends DoubleFoodFactor {
  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = PartialFunction.empty

  override def +(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

case class LimitedFoodFactor() extends SingleFoodFactor {

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: HighFoodFactor      => LimitedHighFoodFactor()
    case _: ToughFoodFactor     => LimitedToughFoodFactor()
    case _: HighToughFoodFactor => LimitedHighToughFoodFactor()
  }

}

case class HighFoodFactor(
    override val affectedGene: GeneKind = Genes.JUMP,
    override val applyToAllBunnies: Boolean = false
) extends SingleFoodFactor
    with FoodFactorOnSingleGene {

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor      => LimitedHighFoodFactor()
    case _: ToughFoodFactor        => HighToughFoodFactor()
    case _: LimitedToughFoodFactor => LimitedHighToughFoodFactor()
  }

}

case class ToughFoodFactor(
    override val affectedGene: GeneKind = Genes.TEETH,
    override val applyToAllBunnies: Boolean = false
) extends SingleFoodFactor
    with FoodFactorOnSingleGene {

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor     => LimitedToughFoodFactor()
    case _: HighFoodFactor        => HighToughFoodFactor()
    case _: LimitedHighFoodFactor => LimitedHighToughFoodFactor()
  }

}

case class LimitedHighFoodFactor(
    override val affectedGene: GeneKind = Genes.JUMP,
    override val applyToAllBunnies: Boolean = true
) extends DoubleFoodFactor
    with FoodFactorOnSingleGene {

  override protected val splitFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => HighFoodFactor()
    case _: HighFoodFactor    => LimitedFoodFactor()
  }

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = { case _: ToughFoodFactor =>
    LimitedHighToughFoodFactor()
  }

}

case class LimitedToughFoodFactor(
    override val affectedGene: GeneKind = Genes.TEETH,
    override val applyToAllBunnies: Boolean = true
) extends DoubleFoodFactor
    with FoodFactorOnSingleGene {

  override protected val splitFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => ToughFoodFactor()
    case _: ToughFoodFactor   => LimitedFoodFactor()
  }

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = { case _: HighFoodFactor =>
    LimitedHighToughFoodFactor()
  }

}

case class HighToughFoodFactor(
    override val firstGeneAffected: GeneKind = Genes.JUMP,
    override val secondGeneAffected: GeneKind = Genes.TEETH
) extends DoubleFoodFactor
    with FactorOnDoubleGene {

  override protected val splitFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: HighFoodFactor  => ToughFoodFactor()
    case _: ToughFoodFactor => HighFoodFactor()
  }

  override protected val concatFunction: PartialFunction[FoodFactor, FoodFactor] = { case _: LimitedFoodFactor =>
    LimitedHighToughFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(firstGeneAffected, bunnies)._1, climate)
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
      lowDamage
    )
    bunnies
  }

}

case class LimitedHighToughFoodFactor(
    override val firstGeneAffected: GeneKind = Genes.JUMP,
    override val secondGeneAffected: GeneKind = Genes.TEETH
) extends TripleFoodFactor
    with FactorOnDoubleGene {

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(firstGeneAffected, bunnies)._1, climate)
    super.applyDamage(filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base), climate)
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.mutated),
      lowDamage
    )
    bunnies
  }

  override protected val splitFunction: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor      => HighToughFoodFactor()
    case _: ToughFoodFactor        => LimitedHighFoodFactor()
    case _: HighFoodFactor         => LimitedToughFoodFactor()
    case _: LimitedHighFoodFactor  => ToughFoodFactor()
    case _: HighToughFoodFactor    => LimitedFoodFactor()
    case _: LimitedToughFoodFactor => HighFoodFactor()
  }

}
