package model.world.disturbingFactors

import engine.SimulationConstants.FactorsConstants.{ WOLF_HIGH_DAMAGE, WOLF_LOW_DAMAGE, WOLF_MEDIUM_DAMAGE }
import model.Bunny.filterBunniesWithAlleles
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.Climate
import model.world.Generation.Population
import model.world.disturbingFactors.FactorTypes.{ FactorKind, WolvesFactorKind }
import model.world.disturbingFactors.FactorsUtils.applyCustomDamage

sealed trait PredatorFactor extends Factor {

  /** @return the low damage that is applied with this Factor */
  protected def lowDamage: Double

  /** @return the high damage that is applied with this Factor */
  protected def highDamage: Double
}

abstract class BasicFactor extends Factor {

  override def applyDamage(bunnies: Population, climate: Climate): Population = applyCustomDamage(bunnies, normalDamage)

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
    with FactorOnDoubleGene {

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
