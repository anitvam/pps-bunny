package it.unibo.pps.bunny.util

/**
 * And Exception with and explanation.
 * @param explanation the string that explains what went wrong
 */
class ExplainedException(explanation: String) extends Exception {
  print(explanation + "\n")
}
