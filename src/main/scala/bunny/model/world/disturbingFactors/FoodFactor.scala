package bunny.model.world.disturbingFactors

import bunny.engine.SimulationConstants.FactorsConstants._
import bunny.model.Bunny.filterBunniesWithAlleles
import bunny.model.{ Bunny, InvalidFoodFactor }
import bunny.model.genome.Genes
import bunny.model.genome.Genes.GeneKind
import bunny.model.world.Climate
import bunny.model.world.Generation.Population
import bunny.model.world.disturbingFactors.FactorTypes._
import bunny.model.world.disturbingFactors.FactorsUtils._

sealed trait FoodFactor extends BasicFactor {

  override val normalDamage: Double = FOOD_FACTOR_NORMAL_DAMAGE
  override def factorType: FactorKind = FoodFactorKind

  val lowDamage: Double = FOOD_FACTOR_LOW_DAMAGE

  def combineWith(foodFactor: FoodFactor): FoodFactor
  def isCombined: Boolean
  def removeSubFactor(foodFactor: FoodFactor): FoodFactor
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

  val concatFactor: PartialFunction[FoodFactor, FoodFactor]

  override def combineWith(foodFactor: FoodFactor): FoodFactor = {
    if (concatFactor isDefinedAt foodFactor) concatFactor(foodFactor) else throw new InvalidFoodFactor()
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

abstract class DoubleFoodFactor(override val isCombined: Boolean = true) extends SingleFoodFactor {
  val decoupleFactor: PartialFunction[FoodFactor, FoodFactor]

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = {
    if (decoupleFactor isDefinedAt foodFactor) decoupleFactor(foodFactor) else throw new InvalidFoodFactor()
  }

}

abstract class TripleFoodFactor() extends DoubleFoodFactor {
  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = PartialFunction.empty

  override def combineWith(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

case class LimitedFoodFactor() extends SingleFoodFactor {

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: HighFoodFactor  => LimitedHighFoodFactor()
    case _: ToughFoodFactor => LimitedToughFoodFactor()
  }

}

case class HighFoodFactor(
    override val affectedGene: GeneKind = Genes.JUMP,
    override val applyToAllBunnies: Boolean = false
) extends SingleFoodFactor
    with FoodFactorOnSingleGene {

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => LimitedHighFoodFactor()
    case _: ToughFoodFactor   => LimitedToughFoodFactor()
  }

}

case class ToughFoodFactor(
    override val affectedGene: GeneKind = Genes.TEETH,
    override val applyToAllBunnies: Boolean = false
) extends SingleFoodFactor
    with FoodFactorOnSingleGene {

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => LimitedToughFoodFactor()
    case _: HighFoodFactor    => HighToughFoodFactor()
  }

}

case class LimitedHighFoodFactor(
    override val affectedGene: GeneKind = Genes.JUMP,
    override val applyToAllBunnies: Boolean = true
) extends DoubleFoodFactor
    with FoodFactorOnSingleGene {

  override val decoupleFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => HighFoodFactor()
    case _: HighFoodFactor    => LimitedFoodFactor()
  }

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = { case _: ToughFoodFactor =>
    LimitedHighToughFoodFactor()
  }

}

case class LimitedToughFoodFactor(
    override val affectedGene: GeneKind = Genes.TEETH,
    override val applyToAllBunnies: Boolean = true
) extends DoubleFoodFactor
    with FoodFactorOnSingleGene {

  override val decoupleFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => ToughFoodFactor()
    case _: ToughFoodFactor   => LimitedFoodFactor()
  }

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = { case _: HighFoodFactor =>
    LimitedHighToughFoodFactor()
  }

}

case class HighToughFoodFactor(
    override val firstGeneAffected: GeneKind = Genes.JUMP,
    override val secondGeneAffected: GeneKind = Genes.TEETH
) extends DoubleFoodFactor
    with FactorOnDoubleGene {

  override val decoupleFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: HighFoodFactor  => ToughFoodFactor()
    case _: ToughFoodFactor => HighFoodFactor()
  }

  override val concatFactor: PartialFunction[FoodFactor, FoodFactor] = { case _: LimitedFoodFactor =>
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

  override val decoupleFactor: PartialFunction[FoodFactor, FoodFactor] = {
    case _: LimitedFoodFactor => HighToughFoodFactor()
    case _: ToughFoodFactor   => LimitedHighFoodFactor()
    case _: HighFoodFactor    => LimitedToughFoodFactor()
  }

}
