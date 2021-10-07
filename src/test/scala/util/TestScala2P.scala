package util

import alice.tuprolog.Term
import util.PimpScala.RichOption
import util.Scala2P._

object TestScala2P extends App {
  val engine: Term => Option[Term] = SingleSolutionPrologEngine("prolog/pedigree_dim.pl")
  val solution = engine("pedigree_dimensions(3500, 3627, 2, 3, 0.8, 80, 40, 30, BSF, G)")
  solution --> {println(_)}
  println(extractTerm(solution.get, 8))
  println(extractTerm(solution.get, 9))
}
