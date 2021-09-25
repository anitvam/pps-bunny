package model.world

import engine.SimulationConstants._
import model.Bunny
import model.genome.Alleles.AlleleKind
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.FactorsUtils.{ filterBunniesWithAlleles, killPercentageBunnies }
import model.world.Generation.Population
import scala.util.Random._

sealed trait Factor {

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
  override val normalDamage: Double = FOOD_FACTOR_NORMAL_DAMAGE
  private val lowDamage: Double = FOOD_FACTOR_LOW_DAMAGE

  /**
   * The LimitedFood Factor is introduced into the environment
   * @return
   *   true if LimitedFood is active
   */
  var isLimitedFood: Boolean

  /**
   * The HighFood Factor is introduced into the environment
   * @return
   *   true if HighFood is active
   */
  var isHighFood: Boolean

  /** @return The GeneKind afflicted by HighFood */
  val highFoodDamagedGene: GeneKind = Genes.JUMP

  /**
   * The ToughFood Factor is introduced into the environment
   * @return
   *   true if ToughFood is active
   */
  var isToughFood: Boolean

  /** @return The GeneKind afflicted by ToughFood */
  val toughFoodDamagedGene: GeneKind = Genes.TEETH

  abstract override def applyDamage(bunnies: Population, climate: Climate): Population = {
    val bunniesSplitByHighFoodGene = Bunny.splitBunniesByGene(highFoodDamagedGene, bunnies)
    val bunniesSplitByToughFoodGene = Bunny.splitBunniesByGene(toughFoodDamagedGene, bunnies)

    (isHighFood, isLimitedFood, isToughFood) match {
      case (true, false, false) => super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
      case (false, true, false) => super.applyDamage(bunnies, climate)
      case (false, false, true) => super.applyDamage(bunniesSplitByToughFoodGene._1, climate)
      case (true, true, false) =>
        super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
        killPercentageBunnies(bunniesSplitByHighFoodGene._2, lowDamage)
      case (true, false, true) =>
        super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
        killPercentageBunnies(
          filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.base),
          lowDamage
        )
      case (false, true, true) =>
        super.applyDamage(bunniesSplitByToughFoodGene._1, climate)
        killPercentageBunnies(bunniesSplitByToughFoodGene._2, lowDamage)
      case (true, true, true) =>
        super.applyDamage(bunniesSplitByHighFoodGene._1, climate)
        super.applyDamage(
          filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.base),
          climate
        )
        killPercentageBunnies(
          filterBunniesWithAlleles(bunnies, highFoodDamagedGene.mutated, toughFoodDamagedGene.mutated),
          lowDamage
        )
    }
    bunnies
  }

}

object Factor {

  abstract class BasicFactor extends Factor {

    override def applyDamage(bunnies: Population, climate: Climate): Population =
      killPercentageBunnies(bunnies, normalDamage)

  }

  case class FoodFactorImpl(
      override var isHighFood: Boolean,
      override var isLimitedFood: Boolean,
      override var isToughFood: Boolean
  ) extends BasicFactor
      with FoodFactor

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

  case class UnfriendlyClimate(
      override val normalDamage: Double = UNFRIENDLY_CLIMATE_DAMAGE,
      afflictedGene: GeneKind = Genes.FUR_LENGTH
  ) extends ClimateFactor
      with FactorWithOneGene {

    override protected def summerAction(bunnies: Population): Population = {
      val splitBunnies = Bunny.splitBunniesByGene(afflictedGene, bunnies)
      killPercentageBunnies(splitBunnies._2, normalDamage) ++ splitBunnies._1
    }

    override protected def winterAction(bunnies: Population): Population = {
      val splitBunnies = Bunny.splitBunniesByGene(afflictedGene, bunnies)
      killPercentageBunnies(splitBunnies._1, normalDamage) ++ splitBunnies._2
    }

  }

  case class Wolves(
      normalDamage: Double = WOLF_MEDIUM_DAMAGE,
      lowDamage: Double = WOLF_LOW_DAMAGE,
      highDamage: Double = WOLF_HIGH_DAMAGE
  ) extends ClimateFactor
      with PredatorFactor {

    private val firstGeneAffected: GeneKind = Genes.FUR_COLOR
    private val secondGeneAffected: GeneKind = Genes.EARS

    override def summerAction(bunnies: Population): Population = manageKillBunnies(
      bunnies,
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
      filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.mutated)
    )

    override def winterAction(bunnies: Population): Population = manageKillBunnies(
      bunnies,
      filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.base),
      filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.mutated)
    )

    private def manageKillBunnies(
        bunnies: Population,
        bunniesWithLowDamage: Population,
        bunniesWithHighDamage: Population
    ): Population = {
      killPercentageBunnies(bunniesWithLowDamage, lowDamage)
      killPercentageBunnies(bunnies diff bunniesWithLowDamage diff bunniesWithHighDamage, normalDamage)
      killPercentageBunnies(bunniesWithHighDamage, highDamage)
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
  def killPercentageBunnies(bunnies: Population, percentage: Double): Population = {
    shuffle(bunnies) take (bunnies.length * percentage).toInt foreach { _.alive = false }
    println("Ho ucciso " + (bunnies.length * percentage).toInt + " conigli c: da una lista di " + bunnies.length)
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

}
