package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.engine.SimulationConstants.FactorsConstants.UNFRIENDLY_CLIMATE_DAMAGE
import it.unibo.pps.bunny.model.bunny.Bunny
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.{ Climate, Summer }
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorsUtils.applyCustomDamage

abstract class ClimateFactor() extends BasicFactor {

  override def applyDamage(bunnies: Population, climate: Climate): Population =
    if (climate == Summer) summerAction(bunnies) else winterAction(bunnies)

  /**
   * Action performed on a [[Population]] of bunnies on Summer
   * @param bunnies
   *   the [[Population]]
   * @return
   *   the [[Population]] updated
   */
  protected def summerAction(bunnies: Population): Population

  /**
   * Action performed on a [[Population]] of bunnies on Winter
   * @param bunnies
   *   the [[Population]]
   * @return
   *   the [[Population]] updated
   */
  protected def winterAction(bunnies: Population): Population

}

case class UnfriendlyClimateFactor(
    override val normalDamage: Double = UNFRIENDLY_CLIMATE_DAMAGE,
    override val factorKind: FactorKind = UnfriendlyClimateFactorKind,
    override val affectedGene: GeneKind = Genes.FUR_LENGTH
) extends ClimateFactor
    with FactorOnSingleGene {

  override protected def summerAction(bunnies: Population): Population = {
    val splitBunnies = Bunny.splitBunniesByGene(affectedGene, bunnies)
    applyCustomDamage(splitBunnies._2, normalDamage) ++ splitBunnies._1
  }

  override protected def winterAction(bunnies: Population): Population = {
    val splitBunnies = Bunny.splitBunniesByGene(affectedGene, bunnies)
    applyCustomDamage(splitBunnies._1, normalDamage) ++ splitBunnies._2
  }

}
