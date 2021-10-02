package model.world.disturbingFactors

import model.genome.Alleles.AlleleKind
import model.genome.Genes.GeneKind
import model.world.Climate
import model.world.Generation.Population

import scala.util.Random

trait Factor {
  import model.world.disturbingFactors.FactorTypes._

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

trait FactorWithOneGene extends BasicFactor {

  /**
   * The afflicted Gene of this Factor
   * @return
   *   a Option[GeneKind]
   */
  val affectedGene: GeneKind

}

trait FactorWithTwoGenes extends BasicFactor {
  val firstGeneAffected: GeneKind
  val secondGeneAffected: GeneKind
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

object FactorTypes extends Enumeration {
  type FactorKind = Value

  val WolvesFactorKind, UnfriendlyClimateFactorKind, FoodFactorKind = Value
}
