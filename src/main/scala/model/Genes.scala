package model

object Genes extends Enumeration {
  object Alleles extends Enumeration{
    type AlleleKind = Value
    protected case class AllelesVal(var isDominant: Option[Boolean] = Option.empty) extends super.Val
    import scala.language.implicitConversions
    implicit def valueToAllelesVal(x: Value): AllelesVal = x.asInstanceOf[AllelesVal]

    val WHITE_FUR = AllelesVal()
    val BROWN_FUR = AllelesVal()
    val LONG_FUR = AllelesVal()
    val SHORT_FUR = AllelesVal()
    val LONG_TEETH = AllelesVal()
    val SHORT_TEETH = AllelesVal()
    val HIGH_EARS = AllelesVal()
    val LOW_EARS = AllelesVal()
    val HIGH_JUMP = AllelesVal()
    val LOW_JUMP = AllelesVal()
  }

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

  def getGeneKind(kind:AlleleKind) = ???
}


