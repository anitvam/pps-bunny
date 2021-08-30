package model
import model.AlleleKind.AlleleKindType
import model.GeneKind.GeneKindType

case class Phenotype(attributes: Map[GeneKindType, AlleleKindType])

case class Genotype(genes: List[Gene]) {
  def getPhenotype(): Phenotype = Phenotype(genes.map(g => (g.kind, g.getAttribute())).toMap)
  def + (gene :Gene) = ???
}

case class Allele(kind: AlleleKindType,
                  isDominant: Option[Boolean] = Option.empty,
                  isMutated: Option[Boolean] = Option.empty){

  def getCaseSensitiveLetter(letter: String): String = {
    if (isDominant.isDefined) {
      if (isDominant.get) letter.toUpperCase else letter.toLowerCase
    } else ""
  }
}

case class Gene(kind: GeneKindType,
                momAllele: Allele,
                dadAllele: Allele) {
  def getAttribute(): AlleleKindType =
    if (momAllele.kind == dadAllele.kind || momAllele.isDominant.getOrElse(false)) momAllele.kind else dadAllele.kind

  def getLetters(): String = momAllele.getCaseSensitiveLetter(kind.letter) + dadAllele.getCaseSensitiveLetter(kind.letter)

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base) && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new IllegalAlleleException("Gene initialization: one of the Alleles (momAllele or dadAllele) has a kind which is not suitable with the kind of the Gene!")
}
