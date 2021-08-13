
import org.scalatest.{FlatSpec, Matchers}

class TestClass extends FlatSpec with Matchers {
  "An empty set" should "have size 0" in {
    assert(Set.empty.isEmpty)
  }

  it should "raise NoSuchElementException for head" in {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }
  }
}
