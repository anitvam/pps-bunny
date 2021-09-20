package view.scalaFX.utilities

object PimpScala {
  implicit class RichOption[A, B](option: Option[A]) {
    def --> (consumer: A => Unit): Unit = option.foreach(consumer)
  }
}
