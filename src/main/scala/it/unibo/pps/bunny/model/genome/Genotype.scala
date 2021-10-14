package it.unibo.pps.bunny.model.genome

import it.unibo.pps.bunny.model.genome.Alleles.AlleleKind
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.KindsUtils.isDominanceAssigned
import it.unibo.pps.bunny.model.{ IllegalGenotypeBuildException, InconsistentGenotypeException }

/**
 * Represents Phenotype of the Bunny, which represents visible traits on the bunny
 */
sealed trait Phenotype {

  /** A [[Map]] that contains for each gene the visible [[AlleleKind]] on the bunny */
  protected val visibleTraits: Map[GeneKind, AlleleKind]

  /** An [[Iterable]] on all the [[AlleleKind]] visible on the bunny */
  val values: Iterable[AlleleKind] = visibleTraits.values

  /** A method to get the [[AlleleKind]] for a specific [[GeneKind]] directly, without using visibleTraits */
  def apply(gk: GeneKind): AlleleKind = visibleTraits(gk)

  /**
   * @param ak
   *   the specified [[AlleleKind]]
   * @return
   *   true if the phenotype has the [[AlleleKind]] specified, false otherwise
   */
  def has(ak: AlleleKind): Boolean = values.toSet.contains(ak)
}

object Phenotype {
  def apply(visibleTraits: Map[GeneKind, AlleleKind]): Phenotype = PhenotypeImpl(visibleTraits)
  private case class PhenotypeImpl(override val visibleTraits: Map[GeneKind, AlleleKind]) extends Phenotype
}

/**
 * Represents the genetic heritage of the Bunny, with visible and invisible traits.
 */
sealed trait Genotype {

  /** A [[Map]] that contains all the Genes of the bunny, each one identified from its [[GeneKind]] */
  protected val genes: Map[GeneKind, Gene]

  /** An [[Iterable]] over the Genes of the bunny */
  val values: Iterable[Gene] = genes.values

  /** The [[Phenotype]] of the bunny */
  val phenotype: Phenotype = Phenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))

  /** A method to get the [[Gene]] for a specific [[GeneKind]] directly, without using genes */
  def apply(gk: GeneKind): Gene = genes(gk)

  override def toString: String = genes.values
    .filter(g => isDominanceAssigned(g.kind))
    .map(g => g.momAllele.getLetter + g.dadAllele.getLetter)
    .reduceOption(_ + " " + _)
    .getOrElse("-")

  /** @return the number of mutated alleles in the genotype */
  def mutatedAllelesQuantity: Int =
    genes.values.count(g => g.dadAllele.isInstanceOf[JustMutatedAllele] || g.momAllele.isInstanceOf[JustMutatedAllele])

  /** @return true if there are mutated alleles, otherwise false */
  def isJustMutated: Boolean = mutatedAllelesQuantity > 0

  /**
   * @param geneKind
   *   the kind of genes for which the alleles are required
   * @return
   *   a sequence of standard alleles with the parents kind, useful during the generation of children
   */
  def getStandardAlleles(geneKind: GeneKind): (StandardAllele, StandardAllele) =
    (StandardAllele(genes(geneKind).momAllele.kind), StandardAllele(genes(geneKind).dadAllele.kind))

  if (genes.count(g => g._1 != g._2.kind) > 0) throw new InconsistentGenotypeException(genes)
}

/**
 * Represents a Genotype which many not contain all the Genes of the world, so it's incomplete.
 * @param genes
 *   the Genes of the Genotype
 */
case class PartialGenotype(genes: Map[GeneKind, Gene]) extends Genotype {
  def +(gene: Gene): PartialGenotype = PartialGenotype(genes + (gene.kind -> gene))
}

/**
 * Represents a Genotype which for sure contains all the Genes of the world.
 * @param genes
 *   the Genes of the Genotype
 */
case class CompleteGenotype(genes: Map[GeneKind, Gene]) extends Genotype {
  def +(gene: Gene): CompleteGenotype = CompleteGenotype(genes + (gene.kind -> gene))
  if (Genes.values.count(!genes.keySet.contains(_)) > 0) throw new IllegalGenotypeBuildException
}
