package model

object AllGenes extends Enumeration {
  type GeneType = Value
  object AlleleKind extends Enumeration{
    type AlleleType = Value

    val WHITE_FUR, BROWN_FUR,
    LONG_FUR, SHORT_FUR,
    LONG_TEETH, SHORT_TEETH,
    HIGH_EARS, LOW_EARS,
    HIGH_JUMP, LOW_JUMP = Value
  }

  import AlleleKind.AlleleType
  protected case class GeneKindVal(base: AlleleType,
                                   mutated: AlleleType,
                                   letter: String) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToGeneKindVal(x: Value): GeneKindVal = x.asInstanceOf[GeneKindVal]

  val FUR_COLOR: GeneKindVal =  GeneKindVal(base = AlleleKind.WHITE_FUR,
                                mutated = AlleleKind.BROWN_FUR,
                                letter = "f")
  val FUR_LENGTH: GeneKindVal = GeneKindVal(base = AlleleKind.SHORT_FUR,
                                mutated = AlleleKind.LONG_FUR,
                                letter = "l")
  val TEETH: GeneKindVal =      GeneKindVal(base = AlleleKind.SHORT_TEETH,
                                mutated = AlleleKind.LONG_TEETH,
                                letter = "t")
  val EARS: GeneKindVal =       GeneKindVal(base = AlleleKind.HIGH_EARS,
                                mutated = AlleleKind.LOW_EARS,
                                letter = "e")
  val JUMP: GeneKindVal =       GeneKindVal(base = AlleleKind.LOW_JUMP,
                                mutated = AlleleKind.HIGH_JUMP,
                                letter = "j")
}


