package it.unibo.pps.bunny.util

import alice.tuprolog.{ Prolog, Struct, Term, Theory }

import scala.io.Source

object Scala2P {
  implicit def stringToTerm(s: String): Term = Term.createTerm(s)
  implicit def termToInt(t: Term): Int = t.toString.toInt

  def extractTerm(t: Term, i: Integer): Term = t.asInstanceOf[Struct].getArg(i).getTerm

  def SingleSolutionPrologEngine(resourcePath: String): Term => Option[Term] = { goal =>
    {
      val isComment: String => Boolean = _(0) == "%" (0)
      val clauses = Source.fromResource(resourcePath).getLines().filter(l => l.nonEmpty && !isComment(l)).mkString(" ")
      val engine = new Prolog
      engine.setTheory(new Theory(clauses))
      val solution = engine.solve(goal)
      if (solution.isSuccess) Option(solution.getSolution) else Option.empty
    }
  }

  class PrologCalculationException
      extends ExplainedException(
        "PrologCalculation EXCEPTION: Something went wrong during the dimensions calculation in Prolog."
      )

}
