package it.unibo.pps.bunny.util

import alice.tuprolog.Term
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.util.Scala2P._

object TestScala2P extends App {
  val engine: Term => Option[Term] = SingleSolutionPrologEngine("prolog/pedigree_dim.pl")
  val solution = engine("pedigree_dimensions(3500, 3627, 2, 3, 0.8, 80, 40, 30, BSF, G)")
  solution --> { println(_) }
  println(extractTerm(solution.get, 8))
  println(extractTerm(solution.get, 9))
}
