package model.world.disturbingFactors

import engine.SimulationConstants._
import model.genome.Alleles.AlleleKind
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.{ Climate, Summer }
import model.world.Generation.Population
import model.world.disturbingFactors.FactorsUtils.{ applyCustomDamage, filterBunniesWithAlleles }
import model.Bunny
import model.world.disturbingFactors.FactorTypes._

import scala.util.Random

trait Factor {

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

trait FactorWithOneGene extends Factor {

  /**
   * The afflicted Gene of this Factor
   * @return
   *   a Option[GeneKind]
   */
  val affectedGene: GeneKind
}

trait FactorWithTwoGenes extends Factor {
  val firstGeneAffected: GeneKind
  val secondGeneAffected: GeneKind
}

sealed trait PredatorFactor extends Factor {

  /** @return the low damage that is applied with this Factor */
  protected def lowDamage: Double

  /** @return the high damage that is applied with this Factor */
  protected def highDamage: Double
}

abstract class BasicFactor extends Factor {

  override def applyDamage(bunnies: Population, climate: Climate): Population = applyCustomDamage(bunnies, normalDamage)

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

case class UnfriendlyClimate(
    override val normalDamage: Double = UNFRIENDLY_CLIMATE_DAMAGE,
    override val factorType: FactorKind = UnfriendlyClimateFactorKind,
    override val affectedGene: GeneKind = Genes.FUR_LENGTH
) extends ClimateFactor
    with FactorWithOneGene {

  override protected def summerAction(bunnies: Population): Population = {
    val splitBunnies = Bunny.splitBunniesByGene(affectedGene, bunnies)
    applyCustomDamage(splitBunnies._2, normalDamage) ++ splitBunnies._1
  }

  override protected def winterAction(bunnies: Population): Population = {
    val splitBunnies = Bunny.splitBunniesByGene(affectedGene, bunnies)
    applyCustomDamage(splitBunnies._1, normalDamage) ++ splitBunnies._2
  }

}

case class Wolves(
    override val normalDamage: Double = WOLF_MEDIUM_DAMAGE,
    override val lowDamage: Double = WOLF_LOW_DAMAGE,
    override val highDamage: Double = WOLF_HIGH_DAMAGE,
    override val factorType: FactorKind = WolvesFactorKind,
    override val firstGeneAffected: GeneKind = Genes.FUR_COLOR,
    override val secondGeneAffected: GeneKind = Genes.EARS
) extends ClimateFactor
    with PredatorFactor
    with FactorWithTwoGenes {

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

}
