package model

import model.AlleleKind.AlleleKind
import model.GeneKind.GeneKind

case class Genotype(genes: List[Gene]) {
  def getPhenotype(): Phenotype = Phenotype(genes.map(g => (g.kind, g.getAttribute())).toMap)
  def + (gene :Gene) = ???
}

case class Phenotype(attributes: Map[GeneKind, AlleleKind])

case class Gene(kind: GeneKind,
                momAllele: Allele,
                dadAllele: Allele,
                letters: Option[String] = Option.empty) {
  def getAttribute(): AlleleKind =
    if (momAllele.kind == dadAllele.kind || momAllele.isDominant.getOrElse(false)) momAllele.kind else dadAllele.kind

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base) && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new IllegalAlleleException("Gene initialization: one of the Alleles (momAllele or dadAllele) has a kind which is not suitable with the kind of the Gene!")
}

case class Allele(kind: AlleleKind,
                  isDominant: Option[Boolean] = Option.empty,
                  isMutated: Option[Boolean] = Option.empty)
