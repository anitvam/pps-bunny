package it.unibo.pps.bunny.engine

import cats.effect.IO.unit
import cats.effect.{ IO, Timer }

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{ DurationLong, FiniteDuration }
import scala.language.postfixOps

object GenerationTimer {
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  /** The [[FiniteDuration]] of when the timer is started */
  var start: FiniteDuration = System.currentTimeMillis() millis

  /** @return a [[IO]] description that when evaluated will reset the timer */
  def resetTimer: IO[Unit] = IO { start = System.currentTimeMillis() millis }

  /**
   * Returns a [[IO]] description that when is evaluated it will create an async task that will wait until the time
   * specified
   *
   * @param to
   *   the moment in which the timer sleep ends
   * @return
   *   the [[IO]] monad describing the operation
   */
  def waitFor(to: FiniteDuration): IO[Unit] = waitUntil(getTime - start, to)

  /** The [[FiniteDuration]] of the current system time in milliseconds */
  def getTime: FiniteDuration = System.currentTimeMillis() millis

  /**
   * Returns a [[IO]] description that when evaluated will create an async task that will wait for the interval
   * specified.
   *
   * @param from
   *   the lower bound for the sleeping time interval
   * @param to
   *   the upper bound for the sleeping time interval
   * @return
   *   the [[IO]] monad describing the sleeping operation.
   */
  def waitUntil(from: FiniteDuration, to: FiniteDuration): IO[Unit] = if (from < to) IO.sleep(to - from) else unit
}
