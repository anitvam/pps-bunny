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

  /** The [[AlleleKind]] of this Allele */
  val kind: AlleleKind

  /** This value is true if the Allele is mutated on the actual generation, otherwise false */
  val justMutated: Boolean

<<<<<<< HEAD
  def isDominant: Boolean = kind.isDominant.getOrElse(false)

=======
  /** @return the letter that represents this Allele */
>>>>>>> Refactor of scaladoc inside bunny package
  def getLetter: String =
    if (kind.isDominant ?) {
      if (kind.isDominant.get) getGeneKind(kind).letter.toUpperCase else getGeneKind(kind).letter.toLowerCase
    } else ""

<<<<<<< HEAD
=======
  /** @return true if the Allele is dominant */
  def isDominant: Boolean = kind.isDominant.getOrElse(false)
>>>>>>> Refactor of scaladoc inside bunny package
}

/**
 * Represents a standard Allele.
 * @param kind
 *   the [[AlleleKind]] of this Allele.
 */
case class StandardAllele(kind: AlleleKind) extends Allele {
  override val justMutated: Boolean = false
}

/**
 * Represents an Allele which has just been mutated.
 * @param kind
 *   the [[AlleleKind]] of the Allele.
 */
case class JustMutatedAllele(kind: AlleleKind) extends Allele {
  override val justMutated: Boolean = true
  if (getGeneKind(kind).mutated != kind) throw new InconsistentMutatedAlleleException
}

/**
 * Represents a Gene of a specific Bunny.
 */
trait Gene {

  /** The [[GeneKind]] of this Gene */
  val kind: GeneKind

  /** The [[Allele]] of this Gene inherited from the mom */
  val momAllele: Allele

  /** The [[Allele]] of this Gene inherited from the dad */
  val dadAllele: Allele

  /** @return the [[AlleleKind]] of the visible trait for this gene */
  def getVisibleTrait: AlleleKind = if (isHomozygous || momAllele.isDominant) momAllele.kind else dadAllele.kind

<<<<<<< HEAD
=======
  /** @return true if the Gene is homozygous, otherwise false */
  def isHomozygous: Boolean = momAllele.kind == dadAllele.kind

  /** @return the string representation of this Gene */
>>>>>>> Refactor of scaladoc inside bunny package
  def getLetters: String = momAllele.getLetter + dadAllele.getLetter

  private def isHomozygous: Boolean = momAllele.kind == dadAllele.kind
}

object Gene {

  /** Checks if the the specified GeneKind and the kind of the Allele are consistent. */
  private val checkKind: (GeneKind, Allele) => Boolean = (kind, allele) => getGeneKind(allele.kind) == kind

  def apply(kind: GeneKind, momAllele: Allele, dadAllele: Allele): Gene = {
    if (!(checkKind(kind, momAllele) && checkKind(kind, dadAllele))) throw new InconsistentAlleleException
    GeneImpl(kind, momAllele, dadAllele)
  }

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
