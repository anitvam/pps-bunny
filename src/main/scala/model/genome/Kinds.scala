package model.genome

import model.MultipleDominanceAssignmentException
import model.genome.Alleles.AlleleKind
import model.genome.Genes.GeneKind
import util.PimpScala.{RichOption, RichSeq}

import scala.language.{implicitConversions, postfixOps}

/**
 * An Enumeration for all the Alleles present in the World.
 */
object Alleles extends Enumeration {
  type AlleleKind = Value
  implicit def valueToAllelesVal(x: Value): AllelesVal = x.asInstanceOf[AllelesVal]
  implicit def valueToString(x: Value): String = x.prettyName

  val WHITE_FUR: AllelesVal = AllelesVal("Pelo Bianco")
  val BROWN_FUR: AllelesVal = AllelesVal("Pelo Marrone")
  val LONG_FUR: AllelesVal = AllelesVal("Pelo Lungo")
  val SHORT_FUR: AllelesVal = AllelesVal("Pelo Corto")
  val LONG_TEETH: AllelesVal = AllelesVal("Denti Lunghi")
  val SHORT_TEETH: AllelesVal = AllelesVal("Denti Corti")
  val HIGH_EARS: AllelesVal = AllelesVal("Orecchie Alte")
  val LOW_EARS: AllelesVal = AllelesVal("Orecchie Basse")
  val HIGH_JUMP: AllelesVal = AllelesVal("Salto Alto")
  val LOW_JUMP: AllelesVal = AllelesVal("Salto Basso")

  /**
   * The information each AlleleKind must have
   * @param prettyName
   *   the name of each allele, that will be shown to the user
   */
  protected case class AllelesVal(prettyName: String) extends super.Val {
    private var dominant: Option[Boolean] = Option.empty
    def resetDominance(): Unit = dominant = Option.empty

    def setDominance(cond: Boolean): Unit =
      if (dominant ?) throw new MultipleDominanceAssignmentException else dominant = Option(cond)

    def isDominant: Option[Boolean] = dominant
  }

}

/**
 * An Enumeration for all the Genes present in the World, which every Bunny must have.
 */
object Genes extends Enumeration {
  type GeneKind = Value
  import Alleles.AlleleKind

  /**
   * The information each GeneKind must have.
   * @param base
   *   the base Allele of the gene
   * @param mutated
   *   the mutated Allele of the gene
   * @param letter
   *   the letter which corresponds to this gene
   */
  protected case class GenesVal(base: AlleleKind, mutated: AlleleKind, letter: String, prettyName: String)
      extends super.Val

  import scala.language.implicitConversions
  implicit def valueToGenesVal(x: Value): GenesVal = x.asInstanceOf[GenesVal]
  implicit def valueToString(x: Value): String = x.prettyName

  val FUR_COLOR: GenesVal =
    GenesVal(base = Alleles.WHITE_FUR, mutated = Alleles.BROWN_FUR, letter = "f", prettyName = "Colore pelliccia")

  val FUR_LENGTH: GenesVal =
    GenesVal(base = Alleles.SHORT_FUR, mutated = Alleles.LONG_FUR, letter = "l", prettyName = "Lunghezza pelo")

  val TEETH: GenesVal =
    GenesVal(base = Alleles.SHORT_TEETH, mutated = Alleles.LONG_TEETH, letter = "t", prettyName = "Lunghezza denti")

  val EARS: GenesVal =
    GenesVal(base = Alleles.HIGH_EARS, mutated = Alleles.LOW_EARS, letter = "e", prettyName = "Orecchie")

  val JUMP: GenesVal =
    GenesVal(base = Alleles.LOW_JUMP, mutated = Alleles.HIGH_JUMP, letter = "j", prettyName = "Altezza salto")

}

object KindsUtils {

  /**d
   * Function to get a random AlleleKind for the specified GeneKind.
   */
  val randomAlleleKindChooser: GeneKind => AlleleKind = geneKind => Seq(geneKind.base, geneKind.mutated).random

  /**
   * Randomly chooses one AlleleKind as Dominant for each GeneKind
   */
  def assignRandomDominance(): Unit =
    Genes.values.foreach(gk => setAlleleDominance(randomAlleleKindChooser(gk)))


  /**
   * Sets an AlleleKind as dominant for a specific GeneKind.
   *
   * @param alleleKind
   *   the AlleleKind that has to be set as dominant
   */
  def setAlleleDominance(alleleKind: AlleleKind): Unit = {
    alleleKind.setDominance(true)
    getAlternativeAlleleKind(alleleKind).setDominance(false)
  }

  /**
   * @param alleleKind
   *   the AlleleKind of which the GeneKind is needed
   * @return
   *   the GeneKind uniquely associated with this AlleleKind
   */
  def getGeneKind(alleleKind: AlleleKind): GeneKind =
    Genes.values.filter(gk => gk.base == alleleKind || gk.mutated == alleleKind).firstKey

  /**
   * @param alleleKind
   *   the AlleleKind of which alternative AlleleKind is needed
   * @return
   *   the AlleleKind uniquely associated with the specified AlleleKind
   */
  def getAlternativeAlleleKind(alleleKind: AlleleKind): AlleleKind = {
    val geneKind = getGeneKind(alleleKind)
    if (geneKind.base == alleleKind) geneKind.mutated else geneKind.base
  }

  /**
   * Reset dominance of all Alleles.
   */
  def resetDominance(): Unit = Alleles.values.foreach(_.resetDominance())
}
