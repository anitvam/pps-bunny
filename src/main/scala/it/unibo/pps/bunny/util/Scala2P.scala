package it.unibo.pps.bunny.util

import alice.tuprolog.{ Prolog, Struct, Term, Theory }
import scala.io.Source

object Scala2P {
  implicit def stringToTerm(s: String): Term = Term.createTerm(s)
  implicit def termToInt(t: Term): Int = t.toString.toInt

  /**
   * Method to extract a [[Term]] from another [[Term]] specifying its position
   * @param t the initial [[Term]]
   * @param i the position of the [[Term]] that need to be extracted
   * @return the [[Term]] in the position i of t
   */
  def extractTerm(t: Term, i: Integer): Term = t.asInstanceOf[Struct].getArg(i).getTerm

  /**
   * Method that creates a Prolog Engine to solve a goal with a single solution.
   * @param resourcePath the path ot the file with the prolog rules
   * @return An [[Option]] of a term with the solution if there is one, an empty [[Option]] if there is not
   */
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

  /**
   * An Exception that occurs when there should be solution but there is not.
   */
  class PrologCalculationException
      extends ExplainedException(
        "PrologCalculation EXCEPTION: Something went wrong during the dimensions calculation in Prolog."
      )

}
