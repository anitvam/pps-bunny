package model
import model.Alleles.AlleleKind
import model.Genes.GeneKind
import model.GenesUtils.getAlternativeAlleleKind

case class Phenotype(visibleTraits: Map[GeneKind, AlleleKind])

case class Genotype(genes: Map[GeneKind, Gene]) {
  def getPhenotype: Phenotype = Phenotype(genes.map(entry => (entry._1, entry._2.getVisibleTrait)))
  def +(gene: Gene): Map[GeneKind, Gene] = genes + (gene.kind -> gene)
  def completed(): Unit = {
    if (Genes.values.count(!genes.keySet.contains(_)) > 0)
      throw new IllegalGenotypeCompletedException
  }

  if (genes.count(g => g._1 != g._2.kind) > 0)
    throw new GenotypeInconsistencyException(genes)
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
    throw new IllegalAlleleArgumentException
}

object GenotypeUtils {
  def setAlleleDominance(alleleKind: AlleleKind): Unit = {
    if (alleleKind.locked || getAlternativeAlleleKind(alleleKind).locked)
      throw new MultipleDominanceAssignmentException(alleleKind)
    alleleKind.isDominant = Option(true)
    alleleKind.locked = true
    getAlternativeAlleleKind(alleleKind).isDominant = Option(false)
    getAlternativeAlleleKind(alleleKind).locked = true
  }
}