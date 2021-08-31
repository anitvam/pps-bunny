package model
import model.Genes.Alleles.AlleleKind
import model.Genes.{GeneKind, getAlternativeAlleleKind}

case class Phenotype(visibleTraits: Map[GeneKind, AlleleKind])

case class Genotype(genes: Map[GeneKind, Gene]) {
  def getPhenotype: Phenotype = Phenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))
  def +(gene: Gene): Map[GeneKind, Gene] = genes + (gene.kind -> gene)
  def completed(): Unit = {
    if (Genes.values.count(!genes.keySet.contains(_)) > 0)
      throw new IllegalGenotypeException("Genotype initialization EXCEPTION: the Genotype must contain all the Genes")
  }

  if (genes.count(g => g._1 != g._2.kind) > 0)
    throw new IllegalGenotypeException("Genotype initialization EXCEPTION: the GeneType in the key must be coherent "
      + "with the kind in the corresponding Gene\n"
      + genes.filter(g => g._1 == g._2.kind))
}

case class Allele(kind: AlleleKind,
                  isMutated: Option[Boolean] = Option.empty) {
  def getCaseSensitiveLetter(letter: String): String = {
    if (kind.isDominant.isDefined) {
      if (kind.isDominant.get) letter.toUpperCase else letter.toLowerCase
    } else ""
  }
}

case class Gene(kind: GeneKind,
                momAllele: Allele,
                dadAllele: Allele) {
  def getVisibleTrait: AlleleKind =
    if (momAllele.kind == dadAllele.kind || momAllele.kind.isDominant.getOrElse(false)) momAllele.kind else dadAllele.kind
  def getLetters: String = momAllele.getCaseSensitiveLetter(kind.letter) + dadAllele.getCaseSensitiveLetter(kind.letter)

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base)
    && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new IllegalAlleleException("Gene initialization EXCEPTION: one of the Alleles (momAllele or dadAllele) has a kind which is not suitable with the kind of the Gene!")
}

object GenotypeUtils {
  def setAlleleDominance(alleleKind: AlleleKind): Unit = {
    alleleKind.isDominant = Option(true)
    getAlternativeAlleleKind(alleleKind).isDominant = Option(false)
  }
}