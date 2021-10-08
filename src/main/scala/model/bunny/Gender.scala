package model.bunny
import scala.language.implicitConversions

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
