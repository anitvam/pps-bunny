package model
import Alleles.AlleleKind
import Genes.GeneKind

/**
 * Represents Phenotype of the Bunny, which is the visible traits.
 */
sealed trait Phenotype {
  val visibleTraits: Map[GeneKind, AlleleKind]
}

/**
 * Represents a standard Phenotype.
 * @param visibleTraits traits that need to be shown
 */
case class StandardPhenotype(visibleTraits: Map[GeneKind, AlleleKind]) extends Phenotype

/**
 * Represents the genetic heritage of the Bunny, with visible and invisible traits.
 */
sealed trait Genotype {
  val genes: Map[GeneKind, Gene]
  def getPhenotype: Phenotype = StandardPhenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))
  if (genes.count(g => g._1 != g._2.kind) > 0)
    throw new InconsistentGenotypeException(genes)
}

/**
 * Represents a Genotype which many not contain all the Genes of the world, so it's incomplete.
 * @param genes the Genes of the Genotype
 */
case class PartialGenotype(genes: Map[GeneKind, Gene]) extends Genotype{
  def + (gene: Gene): PartialGenotype = PartialGenotype(genes + (gene.kind -> gene))
}

/**
 * Represents a Genotype which for sure contains all the Genes of the world.
 * @param genes the Genes of the Genotype
 */
case class CompletedGenotype(genes: Map[GeneKind, Gene]) extends Genotype{
  def + (gene: Gene): CompletedGenotype = CompletedGenotype(genes + (gene.kind -> gene))
  if (Genes.values.count(!genes.keySet.contains(_)) > 0)
    throw new IllegalGenotypeBuildException
}