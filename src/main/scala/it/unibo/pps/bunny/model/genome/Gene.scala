package it.unibo.pps.bunny.model.genome

import it.unibo.pps.bunny.model.genome.Alleles.AlleleKind
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.KindsUtils.getGeneKind
import it.unibo.pps.bunny.model.{ InconsistentAlleleException, InconsistentMutatedAlleleException }
import it.unibo.pps.bunny.util.PimpScala.RichOption

import scala.language.postfixOps

/**
 * Represents an Allele of a Gene of a specific Bunny.
 */
sealed trait Allele {
  val kind: AlleleKind
  val justMutated: Boolean

  def isDominant: Boolean = kind.isDominant.getOrElse(false)

  def getLetter: String =
    if (kind.isDominant ?) {
      if (kind.isDominant.get) getGeneKind(kind).letter.toUpperCase else getGeneKind(kind).letter.toLowerCase
    } else ""
}

/**
 * Represents a standard allele.
 * @param kind
 *   the kind of the Allele.
 */
case class StandardAllele(kind: AlleleKind) extends Allele {
  override val justMutated: Boolean = false
}

/**
 * Represents an allele which has just been mutated.
 * @param kind
 *   the kind of the Allele.
 */
case class JustMutatedAllele(kind: AlleleKind) extends Allele {
  override val justMutated: Boolean = true
  if (getGeneKind(kind).mutated != kind) throw new InconsistentMutatedAlleleException
}

/**
 * Represents a Gene of a specific Bunny.
 */
trait Gene {
  val kind: GeneKind
  val momAllele: Allele
  val dadAllele: Allele

  private def isHomozygous: Boolean = momAllele.kind == dadAllele.kind

  def getVisibleTrait: AlleleKind = if (isHomozygous || momAllele.isDominant) momAllele.kind else dadAllele.kind

  def getLetters: String = momAllele.getLetter + dadAllele.getLetter
}

object Gene {

  def apply(kind: GeneKind, momAllele: Allele, dadAllele: Allele): Gene = {
    if (!(checkKind(kind, momAllele) && checkKind(kind, dadAllele))) throw new InconsistentAlleleException
    GeneImpl(kind, momAllele, dadAllele)
  }

  /** Checks if the the specified GeneKind and the kind of the Allele are consistent.*/
  private val checkKind: (GeneKind, Allele) => Boolean = (kind, allele) => getGeneKind(allele.kind) == kind

  /**
   * Represents a Standard Gene of a specific Bunny.
   * @param kind
   *   the kind of Gene
   * @param momAllele
   *   the allele from the mom
   * @param dadAllele
   *   the allele from the dad
   */
  private case class GeneImpl(
      override val kind: GeneKind,
      override val momAllele: Allele,
      override val dadAllele: Allele
  ) extends Gene

}
