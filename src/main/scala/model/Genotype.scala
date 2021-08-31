package model
import model.AllGenes.AlleleKind.AlleleType
import model.AllGenes.GeneType

case class Phenotype(attributes: Map[GeneType, AlleleType])

case class Genotype(genes: Map[GeneType, Gene]) {
  def getPhenotype: Phenotype = Phenotype(genes.map(entry => (entry._1, entry._2.getAttribute)))
  def +(gene: Gene): Map[GeneType, Gene] = genes + (gene.kind -> gene)
  def completed(): Unit = {
    if (AllGenes.values.count(!genes.keySet.contains(_)) > 0)
      throw new IllegalGenotypeException("Genotype initialization EXCEPTION: the Genotype must contain all the Genes")
  }

  if (genes.count(g => g._1 != g._2.kind) > 0)
    throw new IllegalGenotypeException("Genotype initialization EXCEPTION: the GeneType in the key must be coherent "
      + "with the kind in the corresponding Gene\n"
      + genes.filter(g => g._1 == g._2.kind))
}

case class Allele(kind: AlleleType,
                  isDominant: Option[Boolean] = Option.empty,
                  isMutated: Option[Boolean] = Option.empty){

  def getCaseSensitiveLetter(letter: String): String = {
    if (isDominant.isDefined) {
      if (isDominant.get) letter.toUpperCase else letter.toLowerCase
    } else ""
  }
}

case class Gene(kind: GeneType,
                momAllele: Allele,
                dadAllele: Allele) {
  def getAttribute: AlleleType =
    if (momAllele.kind == dadAllele.kind || momAllele.isDominant.getOrElse(false)) momAllele.kind else dadAllele.kind
  def getLetters: String = momAllele.getCaseSensitiveLetter(kind.letter) + dadAllele.getCaseSensitiveLetter(kind.letter)

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base)
    && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new IllegalAlleleException("Gene initialization EXCEPTION: one of the Alleles (momAllele or dadAllele) has a kind which is not suitable with the kind of the Gene!")
}