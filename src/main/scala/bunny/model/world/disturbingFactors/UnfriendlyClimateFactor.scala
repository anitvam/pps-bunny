package bunny.model.world.disturbingFactors

import bunny.engine.SimulationConstants.FactorsConstants.UNFRIENDLY_CLIMATE_DAMAGE
import bunny.model.Bunny
import bunny.model.genome.Genes
import bunny.model.genome.Genes.GeneKind
import bunny.model.world.Generation.Population
import bunny.model.world.{ Climate, Summer }
import bunny.model.world.disturbingFactors.FactorTypes.{ FactorKind, UnfriendlyClimateFactorKind }
import bunny.model.world.disturbingFactors.FactorsUtils.applyCustomDamage

abstract class ClimateFactor() extends BasicFactor {

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
