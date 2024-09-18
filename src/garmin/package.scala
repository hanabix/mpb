package garmin

import org.scalajs.dom.Element
import org.scalajs.dom.URL
import org.scalajs.dom.URLSearchParams

private object URL:
  def unapply(u: URL): Option[(String, URLSearchParams)] =
    Some(u.pathname, u.searchParams)
end URL

private val Param = LiteralExtract[URLSearchParams, String]: key =>
  ps => Option.when(ps.has(key))(ps.get(key))

private val Attribute = LiteralExtract[Element, String]: key =>
  e => Option.when(e.hasAttribute(key))(e.getAttribute(key))

private class LiteralExtract[A, B](f: String => A => Option[B]) extends (String => PartialFunction[A, B]):
  def apply(s: String) = f(s).unlift

extension [A](a: A) private inline def |>[B](f: A => B): B = f(a)
