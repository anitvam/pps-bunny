package model

import model.Alleles.AlleleKind
import model.Genes.GeneKind

import scala.language.implicitConversions
import scala.util.Random

/**
 * An Enumeration for all the Alleles present in the World.
 */
object Alleles extends Enumeration{
  type AlleleKind = Value

  /**
   * The information each AlleleKind must have
   * @param dominant specifies if it's dominant or not, it could be empty it not chosen yet
   * @param locked   show if the dominance has already been chosen (becoming unmodifiable) or not
   */
  protected case class AllelesVal(private var dominant: Option[Boolean] = Option.empty,
                                  var locked: Boolean = false) extends super.Val {
    def setDominance(cond: Boolean): Unit = {
      if (locked) throw new MultipleDominanceAssignmentException else {
        dominant = Option(cond)
        locked = true
      }
    }
    def isDominant: Option[Boolean] = dominant
  }
  implicit def valueToAllelesVal(x: Value): AllelesVal = x.asInstanceOf[AllelesVal]

  val WHITE_FUR: AllelesVal = AllelesVal()
  val BROWN_FUR: AllelesVal = AllelesVal()
  val LONG_FUR: AllelesVal = AllelesVal()
  val SHORT_FUR: AllelesVal = AllelesVal()
  val LONG_TEETH: AllelesVal = AllelesVal()
  val SHORT_TEETH: AllelesVal = AllelesVal()
  val HIGH_EARS: AllelesVal = AllelesVal()
  val LOW_EARS: AllelesVal = AllelesVal()
  val HIGH_JUMP: AllelesVal = AllelesVal()
  val LOW_JUMP: AllelesVal = AllelesVal()
}

/**
 * An Enumeration for all the Genes present in the World, which every Bunny must have.
 */
object Genes extends Enumeration {
  type GeneKind = Value
  import Alleles.AlleleKind

  /**
   * The information each GeneKind must have.
   * @param base    the base Allele of the gene
   * @param mutated the mutated Allele of the gene
   * @param letter  the letter which corresponds to this gene
   */
  protected case class GenesVal(base: AlleleKind,
                                mutated: AlleleKind,
                                letter: String) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToGenesVal(x: Value): GenesVal = x.asInstanceOf[GenesVal]

  val FUR_COLOR: GenesVal =  GenesVal(base = Alleles.WHITE_FUR,
                                mutated = Alleles.BROWN_FUR,
                                letter = "f")
  val FUR_LENGTH: GenesVal = GenesVal(base = Alleles.SHORT_FUR,
                                mutated = Alleles.LONG_FUR,
                                letter = "l")
  val TEETH: GenesVal =      GenesVal(base = Alleles.SHORT_TEETH,
                                mutated = Alleles.LONG_TEETH,
                                letter = "t")
  val EARS: GenesVal =       GenesVal(base = Alleles.HIGH_EARS,
                                mutated = Alleles.LOW_EARS,
                                letter = "e")
  val JUMP: GenesVal =       GenesVal(base = Alleles.LOW_JUMP,
                                mutated = Alleles.HIGH_JUMP,
                                letter = "j")
}

object GenesUtils {
  /**
   * @param alleleKind  the AlleleKind of which the GeneKind is needed
   * @return            the GeneKind uniquely associated with this AlleleKind
   */
  def getGeneKind(alleleKind:AlleleKind): GeneKind =
    Genes.values.filter(gk => gk.base == alleleKind || gk.mutated == alleleKind).firstKey

  /**
   * @param alleleKind  the AlleleKind of which alternative AlleleKind is needed
   * @return            the AlleleKind uniquely associated with this AlleleKind
   */
  def getAlternativeAlleleKind(alleleKind:AlleleKind): AlleleKind = {
    val geneKind = getGeneKind(alleleKind)
    if (getGeneKind(alleleKind).base == alleleKind) geneKind.mutated else geneKind.base
  }

  /**
   * Sets an AlleleKind as dominant for a specific GeneKind.
   * @param alleleKind the AlleleKind that has to be set as dominant
   */
  def setAlleleDominance(alleleKind: AlleleKind): Unit = {
    alleleKind.setDominance(cond = true)
    getAlternativeAlleleKind(alleleKind).setDominance(cond = false)
  }

  /**
   * Randomly chooses one AlleleKind as Dominant for each GeneKind
   */
  def assignRandomDominance(): Unit =
    Genes.values.foreach(gk => setAlleleDominance(List(gk.base, gk.mutated)(Random.nextInt(2))))
}


