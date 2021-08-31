package model

import model.Alleles.AlleleKind
import model.Genes.GeneKind

import scala.language.implicitConversions
import scala.util.Random

object Alleles extends Enumeration{
  type AlleleKind = Value
  protected case class AllelesVal(var isDominant: Option[Boolean] = Option.empty,
                                  var isLocked: Boolean = false) extends super.Val
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

object Genes extends Enumeration {
  type GeneKind = Value
  import Alleles.AlleleKind
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
  def getGeneKind(alleleKind:AlleleKind): GeneKind =
    Genes.values.filter(gk => gk.base == alleleKind || gk.mutated == alleleKind).firstKey

  def getAlternativeAlleleKind(alleleKind:AlleleKind): AlleleKind = {
    val geneKind = getGeneKind(alleleKind)
    if (getGeneKind(alleleKind).base == alleleKind) geneKind.mutated else geneKind.base
  }

  def setAlleleDominance(alleleKind: AlleleKind): Unit = {
    if (alleleKind.isLocked || getAlternativeAlleleKind(alleleKind).isLocked)
      throw new MultipleDominanceAssignmentException(alleleKind)
    alleleKind.isDominant = Option(true)
    alleleKind.isLocked = true
    getAlternativeAlleleKind(alleleKind).isDominant = Option(false)
    getAlternativeAlleleKind(alleleKind).isLocked = true
  }

  def assignRandomDominance(): Unit =
    Genes.values.foreach(gk => setAlleleDominance(List(gk.base, gk.mutated)(Random.nextInt(2))))
}


