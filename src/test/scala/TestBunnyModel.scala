import model.{AlleleKind, GeneKind}
import org.scalatest.{FlatSpec, Matchers}

class TestGeneKind extends FlatSpec with Matchers {
  "Each model.AlleleKind" should "be in max one model.GeneKind" in {
    AlleleKind.values.foreach(anyAlleleKind => {
      assert(GeneKind.values.flatMap(gk => List(gk.base, gk.mutated))
                              .filter(_ == anyAlleleKind)
                              .size <=1)
    })
  }

  "Each model.GeneKind" should "have two different model.AlleleKind as base and muted" in {
    GeneKind.values.foreach(gk => assert(gk.base != gk.mutated))
  }
}
