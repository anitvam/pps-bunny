package it.unibo.pps.bunny.util

/**
 * An Exception with an explanation
 * @param explanation
 *   the string that explains what went wrong
 */
class ExplainedException(explanation: String) extends Exception {
  print(explanation + "\n")
}
