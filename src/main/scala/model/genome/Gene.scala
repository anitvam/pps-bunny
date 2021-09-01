package model.genome

import model.genome.Alleles.AlleleKind
import model.genome.Genes.GeneKind
import model.genome.GenesUtils.getGeneKind
import model.{InconsistentAlleleException, InconsistentMutatedAlleleException}

/**
 * Represents an Allele of a Gene of a specific Bunny.
 */
sealed trait Allele {
  val kind: AlleleKind
  val isMutated: Boolean
  def isDominant = kind.isDominant.getOrElse(false)
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
  override val isMutated: Boolean = false
}

/**
 * Represents an allele which has just been mutated.
 * @param kind the kind of the Allele.
 */
case class JustMutatedAllele(kind:AlleleKind) extends Allele{
  override val isMutated: Boolean = true
  if (getGeneKind(kind).mutated != kind) throw new InconsistentMutatedAlleleException
}

/**
 * Represents a Gene of a specific Bunny.
 */
trait Gene {
  val kind: GeneKind
  val momAllele: Allele
  val dadAllele: Allele

  def isHomozygous: Boolean = momAllele.kind == dadAllele.kind
  def getVisibleTrait: AlleleKind = if (isHomozygous || momAllele.isDominant) momAllele.kind else dadAllele.kind
  def getLetters: String = momAllele.getCaseSensitiveLetter(kind.letter) + dadAllele.getCaseSensitiveLetter(kind.letter)
  private def checkKind(allele: Allele) = allele.kind == kind.base || allele.kind == kind.mutated

  if (!(checkKind(momAllele) && checkKind(dadAllele)))
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
