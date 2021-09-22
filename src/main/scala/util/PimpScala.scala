package util

object PimpScala {

  implicit class RichOption[A, B](option: Option[A]) {
    def --> (consumer: A => Unit): Unit = option.foreach(consumer)
    def ? : Boolean = option.isDefined
  }

}
