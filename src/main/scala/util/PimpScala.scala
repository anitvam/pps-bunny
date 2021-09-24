package util

object PimpScala {

  implicit class RichOption[A](option: Option[A]) {
    def -->(consumer: A => Unit): Unit = option.foreach(consumer)
    def ? : Boolean = option.isDefined
  }
}
