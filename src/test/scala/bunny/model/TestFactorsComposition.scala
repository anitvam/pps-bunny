package bunny.model

import bunny.model.Bunny.generateRandomFirstBunny
import bunny.model.world.Winter
import bunny.model.world.disturbingFactors.{ HighFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor }
import org.scalatest.{ FlatSpec, Matchers }

class TestFactorsComposition extends FlatSpec with Matchers {
  import TestFactorsDamageUtils._

  "A HighFoodFactor" should "be added to a LimitedFoodFactor" in {
    val bunnies: List[Bunny] = List.fill(50)(generateRandomFirstBunny)

    val highFoodFactor = HighFoodFactor()
    val limitedFoodFactor = LimitedFoodFactor()

    val combined = limitedFoodFactor concatFactor highFoodFactor
    assert(combined.isInstanceOf[LimitedHighFoodFactor])
    assert(combined.applyDamage(bunnies, Winter()).count(!_.alive) == countHighLimitedFoodDamage(bunnies))

  }

}
