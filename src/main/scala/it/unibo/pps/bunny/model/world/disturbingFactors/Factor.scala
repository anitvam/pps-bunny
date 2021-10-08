package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Climate
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorsUtils.applyCustomDamage

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

abstract class BasicFactor extends Factor {

  override def applyDamage(bunnies: Population, climate: Climate): Population = applyCustomDamage(bunnies, normalDamage)

}

trait FactorOnSingleGene extends BasicFactor {

  /**
   * The afflicted Gene of this Factor
   * @return
   *   a GeneKind
   */
  val affectedGene: GeneKind

}

trait FactorOnDoubleGene extends BasicFactor {

  /**
   * The first afflicted Gene of this Factor
   * @return
   *   a GeneKind
   */
  val firstGeneAffected: GeneKind

  /**
   * The second afflicted Gene of this Factor
   * @return
   *   a GeneKind
   */
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

}

sealed trait FactorKind
case object WolvesFactorKind extends FactorKind
case object UnfriendlyClimateFactorKind extends FactorKind
case object FoodFactorKind extends FactorKind
