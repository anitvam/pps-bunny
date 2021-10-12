package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.engine.SimulationConstants.FactorsConstants.{
  WOLF_HIGH_DAMAGE, WOLF_LOW_DAMAGE, WOLF_MEDIUM_DAMAGE
}
import it.unibo.pps.bunny.model.bunny.Bunny.filterBunniesWithAlleles
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorsUtils.applyCustomDamage

sealed trait PredatorFactor extends Factor {

  /** @return the low damage that is applied with this Factor */
  protected def lowDamage: Double

  /** @return the high damage that is applied with this Factor */
  protected def highDamage: Double
}

case class WolvesFactor(
    override val normalDamage: Double = WOLF_MEDIUM_DAMAGE,
    override val lowDamage: Double = WOLF_LOW_DAMAGE,
    override val highDamage: Double = WOLF_HIGH_DAMAGE,
    override val factorKind: FactorKind = WolvesFactorKind,
    override val firstGeneAffected: GeneKind = Genes.FUR_COLOR,
    override val secondGeneAffected: GeneKind = Genes.EARS
) extends ClimateFactor
    with PredatorFactor
    with FactorOnDoubleGene {

  override protected def summerAction(bunnies: Population): Population = killBunnies(
    bunnies,
    filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.base),
    filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.mutated)
  )

  override protected def winterAction(bunnies: Population): Population = killBunnies(
    bunnies,
    filterBunniesWithAlleles(bunnies, firstGeneAffected.base, secondGeneAffected.base),
    filterBunniesWithAlleles(bunnies, firstGeneAffected.mutated, secondGeneAffected.mutated)
  )

  /**
   * Kills the specified bunnies on different percentage
   * @param bunnies
   *   the population used to find bunnies on which apply normal damage
   * @param bunniesWithLowDamage
   *   the population on which apply low damage
   * @param bunniesWithHighDamage
   *   the population on which apply high damage
   */
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
