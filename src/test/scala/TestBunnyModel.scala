import org.scalatest.{FlatSpec, Matchers}

class TestGeneKind extends FlatSpec with Matchers {
  "Each AlleleKind" should "be in max one GeneKind" in {
    AlleleKind.values.foreach(anyAlleleKind => {
      assert(GeneKind.values.flatMap(gk => List(gk.base, gk.muted))
                              .filter(_ == anyAlleleKind)
                              .size <=1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    GeneKind.values.foreach(gk => assert(gk.base != gk.muted))
  }
}
