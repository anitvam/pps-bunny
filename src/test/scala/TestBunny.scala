import model.BunnyUtils.getStandardBunny
import model._
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers{
  "Each AlleleKind" should "be in max one GeneKind" in {
    AlleleKind.values.foreach(anyAlleleKind => {
      assert(GeneKind.values.flatMap(gk => List(gk.base, gk.mutated))
        .filter(_ == anyAlleleKind)
        .size <= 1)
    })
  }

  "Each GeneKind" should "have two different AlleleKind as base and muted" in {
    GeneKind.values.foreach(gk => assert(gk.base != gk.mutated))
  }


  "Each Gene" should "should throw an Exception if initialized with Alleles of the wrong kind" in {
    assertThrows[IllegalAlleleException] {
      Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.LONG_FUR), Allele(AlleleKind.BROWN_FUR))
    }
  }

  it should "should be initialized with Alleles of the right kind" in {
    noException should be thrownBy Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.WHITE_FUR))
  }


  "Each StandardBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy getStandardBunny()
  }

  it should "have all kind of Genes" in {
    assert(getStandardBunny().genotype.genes.size == GeneKind.values.size)
  }
}
