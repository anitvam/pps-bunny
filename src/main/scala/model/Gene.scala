package model

import model.Alleles.AlleleKind
import model.Genes.GeneKind

/**
 * Represents an Allele of a Gene of a specific Bunny.
 */
sealed trait Allele {
  val kind: AlleleKind
  val isMutated: Boolean
  def getCaseSensitiveLetter(letter: String): String = {
    if (kind.isDominant.isDefined) {
      if (kind.isDominant.get) letter.toUpperCase else letter.toLowerCase
    } else ""
  }
}

/**
 * Represents a standard allele.
 * @param kind the kind of the Allele.
 */
case class StandardAllele(kind: AlleleKind) extends Allele {
  override val isMutated: Boolean = true
}

/**
 * Represents an allele which has just been mutated.
 * @param kind the kind of the Allele.
 */
case class MutatedAllele(kind:AlleleKind) extends Allele{
  override val isMutated: Boolean = false
}

/**
 * Represents a Gene of a specific Bunny.
 */
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

/**
 *  Represents a Standard Gene of a specific Bunny.
 * @param kind      the kind of Gene
 * @param momAllele the allele from the mom
 * @param dadAllele the allele from the dad
 */
case class StandardGene(kind: GeneKind,
                        momAllele: Allele,
                        dadAllele: Allele) extends Gene
