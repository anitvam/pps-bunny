package model.genome

import model.genome.Alleles.AlleleKind
import model.genome.Genes.GeneKind
import model.{ IllegalGenotypeBuildException, InconsistentGenotypeException }

/**
 * Represents Phenotype of the Bunny, which is the visible traits.
 */
sealed trait Phenotype {
  val visibleTraits: Map[GeneKind, AlleleKind]
  val values: Iterable[AlleleKind] = visibleTraits.values
  def apply(gk: GeneKind): AlleleKind = visibleTraits(gk)
}

object Phenotype {
  def apply(visibleTraits: Map[GeneKind, AlleleKind]): Phenotype = PhenotypeImpl(visibleTraits)
  private case class PhenotypeImpl(override val visibleTraits: Map[GeneKind, AlleleKind]) extends Phenotype
}

/**
 * Represents the genetic heritage of the Bunny, with visible and invisible traits.
 */
sealed trait Genotype {
  val genes: Map[GeneKind, Gene]
  val values: Iterable[Gene] = genes.values
  val phenotype: Phenotype = Phenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))
  def apply(gk: GeneKind): Gene = genes(gk)

  def isJustMutated: Boolean = genes.values.count(g =>
    g.dadAllele.isInstanceOf[JustMutatedAllele] || g.momAllele.isInstanceOf[JustMutatedAllele]
  ) > 0

  if (genes.count(g => g._1 != g._2.kind) > 0) throw new InconsistentGenotypeException(genes)
}

/**
 * Represents a Genotype which many not contain all the Genes of the world, so it's incomplete.
 * @param genes the Genes of the Genotype
 */
case class PartialGenotype(genes: Map[GeneKind, Gene]) extends Genotype {
  def +(gene: Gene): PartialGenotype = PartialGenotype(genes + (gene.kind -> gene))
}

/**
 * Represents a Genotype which for sure contains all the Genes of the world.
 * @param genes the Genes of the Genotype
 */
case class CompletedGenotype(genes: Map[GeneKind, Gene]) extends Genotype {
  def +(gene: Gene): CompletedGenotype = CompletedGenotype(genes + (gene.kind -> gene))
  if (Genes.values.count(!genes.keySet.contains(_)) > 0) throw new IllegalGenotypeBuildException
}
