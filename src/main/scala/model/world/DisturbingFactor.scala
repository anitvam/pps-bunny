package model.world

import engine.SimulationConstants._
import model.{ Bunny, InvalidFoodFactor }
import model.genome.Alleles.AlleleKind
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.Factor.BasicFactor
import model.world.FactorsUtils.{ applyCustomDamage, filterBunniesWithAlleles }
import model.world.Generation.Population

import java.util
import scala.util.Random
import scala.util.Random._

sealed trait Factor {
  import model.world.FactorsUtils.FactorTypes._

  /** @return the normal damage that is applied with this Factor */
  val normalDamage: Double

  /**
   * Method that applies the damage of this Factor on a Population with the given climate
   * @param bunnies
   *   the actual Population
   * @param climate
   *   the actual Climate
   * @return
   *   the remaining Population
   */
  def applyDamage(bunnies: Population, climate: Climate): Population

  /** @return the FactorType of this Factor */
  def factorType: FactorKind
}

sealed trait FactorWithOneGene extends Factor {

  /**
   * The afflicted Gene of this Factor
   * @return
   *   a Option[GeneKind]
   */
  val afflictedGene: GeneKind
}

sealed trait PredatorFactor extends Factor {

  /** @return the low damage that is applied with this Factor */
  protected def lowDamage: Double

  /** @return the high damage that is applied with this Factor */
  protected def highDamage: Double
}

sealed trait FoodFactor extends Factor {
  import model.world.FactorsUtils.FactorTypes._

  override val normalDamage: Double = FOOD_FACTOR_NORMAL_DAMAGE
  val lowDamage: Double = FOOD_FACTOR_LOW_DAMAGE

  override def factorType: FactorKind = FoodFactorKind

  def combineWith(foodFactor: FoodFactor): FoodFactor
  def isCombined: Boolean
  def removeSubFactor(foodFactor: FoodFactor): FoodFactor
}

case class LimitedFoodFactor(override val isCombined: Boolean = false) extends BasicFactor with FoodFactor {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: HighFoodFactor  => LimitedHighFoodFactor()
    case _: ToughFoodFactor => LimitedToughFoodFactor()
    case _                  => throw new InvalidFoodFactor()
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()
}

case class HighFoodFactor(override val afflictedGene: GeneKind = Genes.JUMP, override val isCombined: Boolean = false)
    extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => LimitedHighFoodFactor()
    case _: ToughFoodFactor   => LimitedToughFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(afflictedGene, bunnies)._1, climate)
    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()

}

case class ToughFoodFactor(override val afflictedGene: GeneKind = Genes.TEETH, override val isCombined: Boolean = false)
    extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => LimitedToughFoodFactor()
    case _: HighFoodFactor    => HighToughFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    super.applyDamage(Bunny.splitBunniesByGene(afflictedGene, bunnies)._1, climate)
    bunnies
  }

  override def removeSubFactor(foodFactor: FoodFactor): FoodFactor = throw new UnsupportedOperationException()

}

case class LimitedHighFoodFactor(
    override val afflictedGene: GeneKind = Genes.JUMP,
    override val isCombined: Boolean = true
) extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: ToughFoodFactor => LimitedHighToughFoodFactor()
    case _                  => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(afflictedGene, bunnies)

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
    override val afflictedGene: GeneKind = Genes.TEETH,
    override val isCombined: Boolean = true
) extends BasicFactor
    with FoodFactor
    with FactorWithOneGene {

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: HighFoodFactor => LimitedHighToughFoodFactor()
    case _                 => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByToughFoodGene = Bunny.splitBunniesByGene(afflictedGene, bunnies)

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

case class HighToughFoodFactor(override val isCombined: Boolean = true) extends BasicFactor with FoodFactor {

  /** @return The GeneKind afflicted by HighFood */
  val highFoodDamagedGene: GeneKind = Genes.JUMP

  /** @return The GeneKind afflicted by ToughFood */
  val toughFoodDamagedGene: GeneKind = Genes.TEETH

  override def combineWith(foodFactor: FoodFactor): FoodFactor = foodFactor match {
    case _: LimitedFoodFactor => LimitedHighToughFoodFactor()
    case _                    => throw new InvalidFoodFactor()
  }

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(highFoodDamagedGene, bunnies)
    super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.base),
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

case class LimitedHighToughFoodFactor(override val isCombined: Boolean = true) extends BasicFactor with FoodFactor {

  /** @return The GeneKind afflicted by HighFood */
  val highFoodDamagedGene: GeneKind = Genes.JUMP

  /** @return The GeneKind afflicted by ToughFood */
  val toughFoodDamagedGene: GeneKind = Genes.TEETH

  override def combineWith(foodFactor: FoodFactor): FoodFactor = throw new InvalidFoodFactor()

  override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(highFoodDamagedGene, bunnies)
    super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
    super.applyDamage(
      filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.base),
      climate
    )
    applyCustomDamage(
      filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.mutated),
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

object Factor {

  abstract class BasicFactor extends Factor {

    override def applyDamage(bunnies: Population, climate: Climate): Population =
      applyCustomDamage(bunnies, normalDamage)

  }

  abstract class ClimateFactor extends Factor {

    /**
     * Action performed on a Population of Bunnies on summer
     * @param bunnies
     *   the population
     * @return
     *   the population updated
     */
    protected def summerAction(bunnies: Population): Population

    /**
     * Action performed on a Population of Bunnies on winter
     * @param bunnies
     *   the population
     * @return
     *   the population updated
     */
    protected def winterAction(bunnies: Population): Population

    override def applyDamage(bunnies: Population, climate: Climate): Population =
      if (climate == Summer()) summerAction(bunnies) else winterAction(bunnies)

  }

  import model.world.FactorsUtils.FactorTypes._

  case class UnfriendlyClimate(
      override val normalDamage: Double = UNFRIENDLY_CLIMATE_DAMAGE,
      override val factorType: FactorKind = UnfriendlyClimateFactorKind,
      afflictedGene: GeneKind = Genes.FUR_LENGTH
  ) extends ClimateFactor
      with FactorWithOneGene {

    override protected def summerAction(bunnies: Population): Population = {
      val splitBunnies = Bunny.splitBunniesByGene(afflictedGene, bunnies)
      applyCustomDamage(splitBunnies._2, normalDamage) ++ splitBunnies._1
    }

    override protected def winterAction(bunnies: Population): Population = {
      val splitBunnies = Bunny.splitBunniesByGene(afflictedGene, bunnies)
      applyCustomDamage(splitBunnies._1, normalDamage) ++ splitBunnies._2
    }

  }

  case class Wolves(
      override val normalDamage: Double = WOLF_MEDIUM_DAMAGE,
      override val lowDamage: Double = WOLF_LOW_DAMAGE,
      override val highDamage: Double = WOLF_HIGH_DAMAGE,
      override val factorType: FactorKind = WolvesFactorKind
  ) extends ClimateFactor
      with PredatorFactor {

    private val firstGeneAffected: GeneKind = Genes.FUR_COLOR
    private val secondGeneAffected: GeneKind = Genes.EARS

    override def summerAction(bunnies: Population): Population = killBunnies(
      bunnies,
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
      filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.mutated)
    )

    override def winterAction(bunnies: Population): Population = killBunnies(
      bunnies,
      filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.base),
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.mutated)
    )

    private def killBunnies(
        bunnies: Population,
        bunniesWithLowDamage: Population,
        bunniesWithHighDamage: Population
    ): Population = {
      applyCustomDamage(bunniesWithLowDamage, lowDamage)
      applyCustomDamage(bunnies diff bunniesWithLowDamage diff bunniesWithHighDamage, normalDamage)
      applyCustomDamage(bunniesWithHighDamage, highDamage)
      bunnies
    }

  }

}

object FactorsUtils {

  /**
   * Kills the percentage amount of bunnies on the population
   * @param bunnies
   *   a Population of bunnies
   * @param percentage
   *   a percentage of bunnies
   * @return
   *   the updated population
   */
  def applyCustomDamage(bunnies: Population, percentage: Double): Population = {
    Random.shuffle(bunnies) take (bunnies.length * percentage).round.toInt foreach { _.alive = false }
    bunnies
  }

  /**
   * Method that filter a population of bunnies with two AlleleKinds together
   * @param bunnies
   *   the bunny population
   * @param allele1
   *   the first allele filtered on the population
   * @param allele2
   *   the second allele filtered in population
   * @return
   *   Population the population with the specified AlleleKinds together
   */
  def filterBunniesWithAlleles(bunnies: Population, allele1: AlleleKind, allele2: AlleleKind): Population =
    bunnies filter { bunny =>
      bunny.genotype.phenotype.values.exists(_ == allele1) &&
      bunny.genotype.phenotype.values.exists(_ == allele2)
    }

  object FactorTypes extends Enumeration {
    type FactorKind = Value

    val WolvesFactorKind, UnfriendlyClimateFactorKind, FoodFactorKind = Value

  }

}
