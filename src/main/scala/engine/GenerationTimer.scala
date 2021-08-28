package engine

import cats.effect.IO.unit
import cats.effect.{IO, Timer}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{DurationLong, FiniteDuration}

object GenerationTimer {
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  var start: FiniteDuration = System.currentTimeMillis() millis

  def getTime: FiniteDuration =
    System.currentTimeMillis() millis

  def resetTimer: IO[Unit] = IO {start = System.currentTimeMillis() millis}

  def waitFor(to: FiniteDuration): IO[Unit] = {
    waitUntil(getTime - start, to)
  }

  def waitUntil(from: FiniteDuration, to: FiniteDuration): IO[Unit] =
    if (from < to) {
      IO.sleep(to - from)
    } else unit
}
