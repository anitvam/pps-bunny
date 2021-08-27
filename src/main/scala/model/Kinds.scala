package model

object AlleleKind extends Enumeration{
  type AlleleKind = Value

  val WHITE_FUR, BROWN_FUR,
      LONG_FUR, SHORT_FUR,
      LONG_TEETH, SHORT_TEETH,
      HIGH_EARS, LOW_EARS,
      HIGH_JUMP, LOW_JUMP = Value
}

object GeneKind extends Enumeration {
  type GeneKind = Value
  import AlleleKind.AlleleKind
  protected case class GeneKindVal(base: AlleleKind, mutated: AlleleKind, letter: String) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToGeneKindVal(x: Value): GeneKindVal = x.asInstanceOf[GeneKindVal]

  val FUR_COLOR =   GeneKindVal(base = AlleleKind.WHITE_FUR,
                                mutated = AlleleKind.BROWN_FUR,
                                letter = "f")
  val FUR_LENGTH =  GeneKindVal(base = AlleleKind.SHORT_FUR,
                                mutated = AlleleKind.LONG_FUR,
                                letter = "l")
  val TEETH =       GeneKindVal(base = AlleleKind.SHORT_TEETH,
                                mutated = AlleleKind.LONG_TEETH,
                                letter = "t")
  val EARS =        GeneKindVal(base = AlleleKind.HIGH_EARS,
                                mutated = AlleleKind.LOW_EARS,
                                letter = "e")
  val JUMP =        GeneKindVal(base = AlleleKind.HIGH_JUMP,
                                mutated = AlleleKind.LOW_JUMP,
                                letter = "j")
}


