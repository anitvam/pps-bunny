package model
import Alleles.AlleleKind
import Genes.GeneKind

trait Phenotype {
  val visibleTraits: Map[GeneKind, AlleleKind]
}
case class StandardPhenotype(visibleTraits: Map[GeneKind, AlleleKind]) extends Phenotype

trait Genotype {
  val genes: Map[GeneKind, Gene]
  def getPhenotype: Phenotype = StandardPhenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))
  def +(gene: Gene): Map[GeneKind, Gene] = genes + (gene.kind -> gene)
  if (genes.count(g => g._1 != g._2.kind) > 0)
    throw new InconsistentGenotypeException(genes)
}

case class PartialGenotype(genes: Map[GeneKind, Gene]) extends Genotype
case class CompletedGenotype(genes: Map[GeneKind, Gene]) extends Genotype{
  if (Genes.values.count(!genes.keySet.contains(_)) > 0)
    throw new IllegalGenotypeBuildException
}