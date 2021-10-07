package model.bunny

import scala.util.Random
import scala.language.implicitConversions

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value
  val randomGender: () => Gender = () => Random.shuffle(Gender.values.toList).head
  implicit def genderToString(g: Gender): String = g.toString
}
