import model.{Allele, AlleleKind, Gene, GeneKind, IllegalAlleleException}
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers{
  class TestKinds extends FlatSpec with Matchers {
    "Each model.AlleleKind" should "be in max one model.GeneKind" in {
      AlleleKind.values.foreach(anyAlleleKind => {
        assert(GeneKind.values.flatMap(gk => List(gk.base, gk.mutated))
          .filter(_ == anyAlleleKind)
          .size <= 1)
      })
    }

    "Each model.GeneKind" should "have two different model.AlleleKind as base and muted" in {
      GeneKind.values.foreach(gk => assert(gk.base != gk.mutated))
    }
  }

  class TestBunnyGenes extends FlatSpec with Matchers {
    "Each Gene" should "should throw an Exception if initialized with Alleles of the wrong kind" in {
      assertThrows[IllegalAlleleException] {
        Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.LONG_FUR), Allele(AlleleKind.BROWN_FUR))
      }
    }

    "Each Gene" should "should be initialized with Alleles of the right kind" in {
      noException should be thrownBy Gene(GeneKind.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.WHITE_FUR))
    }
  }
}
