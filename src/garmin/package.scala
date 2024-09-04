package garmin


import org.scalajs.dom.Element
import org.scalajs.dom.URL
import org.scalajs.dom.URLSearchParams


private[garmin] object URL:
  def unapply(u: URL): Option[(String, URLSearchParams)] =
    Some(u.pathname, u.searchParams)
end URL

private[garmin] val Param = LiteralExtract[URLSearchParams, String]: key =>
  ps => Option.when(ps.has(key))(ps.get(key))

private[garmin] val Attribute = LiteralExtract[Element, String]: key =>
  e => Option.when(e.hasAttribute(key))(e.getAttribute(key))

private[garmin] class LiteralExtract[A, B](f: String => A => Option[B]) extends (String => PartialFunction[A, B]):
  def apply(s: String) = f(s).unlift
