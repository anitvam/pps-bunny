package it.unibo.pps.bunny.model

import org.scalatest.{ FlatSpec, Matchers }
import it.unibo.pps.bunny.model.world._
import it.unibo.pps.bunny.model.world.disturbingFactors.Factors

class TestEnvironment extends FlatSpec with Matchers {

  "An empty environment" should "not contains Factors nor Mutations" in {
    val environment = Environment(Summer(), Factors())

    //assert(en)
  }

}
