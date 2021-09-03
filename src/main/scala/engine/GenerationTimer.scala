package engine

import cats.effect.IO.unit
import cats.effect.{IO, Timer}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{DurationLong, FiniteDuration}
import scala.language.postfixOps

object GenerationTimer {
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  /**When the timer is started*/
  var start: FiniteDuration = System.currentTimeMillis() millis

  /**Current System time in milliseconds*/
  def getTime: FiniteDuration = System.currentTimeMillis() millis

  /**Returns a [[IO]] description that when evaluated will reset the timer */
  def resetTimer: IO[Unit] = IO {start = System.currentTimeMillis() millis}

  /**Returns a [[IO]] description that when evaluated will create an async task
   *  that will wait until the time specified
   *
   * @param to the moment until when the timer sleep
   * @return the IO monad describing the operation*/
  def waitFor(to: FiniteDuration): IO[Unit] = waitUntil(getTime - start, to)

  /** Returns a [[IO]] description that when evaluated will create an async task that will wait
   * for the interval specified.
   *
   * @param from the lower bound for of the time interval to be slept
   * @param to the upper bound of the time interval until when sleep.
   * @return the IO monad describing the sleeping operation.
   */
  def waitUntil(from: FiniteDuration, to: FiniteDuration): IO[Unit] =
    if (from < to) IO.sleep(to - from) else unit
}
