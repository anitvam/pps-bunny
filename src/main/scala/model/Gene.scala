package model

import Alleles.AlleleKind
import Genes.GeneKind

trait Allele {
  val kind: AlleleKind
  val isMutated: Boolean
  def getCaseSensitiveLetter(letter: String): String = {
    if (kind.isDominant.isDefined) {
      if (kind.isDominant.get) letter.toUpperCase else letter.toLowerCase
    } else ""
  }
}

case class StandardAllele(kind: AlleleKind) extends Allele {
  override val isMutated: Boolean = true
}
case class MutatedAllele(kind:AlleleKind) extends Allele{
  override val isMutated: Boolean = false
}

trait Gene {
  val kind: GeneKind
  val momAllele: Allele
  val dadAllele: Allele

  def getVisibleTrait: AlleleKind =
    if (momAllele.kind == dadAllele.kind || momAllele.kind.isDominant.getOrElse(false)) momAllele.kind else dadAllele.kind
  def getLetters: String = momAllele.getCaseSensitiveLetter(kind.letter) + dadAllele.getCaseSensitiveLetter(kind.letter)

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base)
    && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new InconsistentAlleleException
}

case class StandardGene(kind: GeneKind,
                        momAllele: Allele,
                        dadAllele: Allele) extends Gene
