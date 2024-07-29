import scala.scalajs.js
import org.scalajs.dom.*

type Cast[A] = A & js.Dynamic

type Route = PartialFunction[(URL, Seq[MutationRecord]), Unit]

trait MetersPerBeat[A] extends (A => Double):
  extension (a: A) inline def mpb = this(a)

trait HoverText[A] extends (A => String):
  extension (a: A) inline def text = this(a)

