package core

import scala.scalajs.js

opaque type Timestamp = String
object Timestamp:
  private trait Ext extends js.Object:
    def toLocaleDateString(locale: String, option: js.Any): String

  def apply(s: String): Timestamp = s

  extension (t: Timestamp)
    def gmtDateString(locale: String): String =
      val d   = new js.Date(t)
      val gmt = new js.Date(d.getTime() - (d.getTimezoneOffset() * 60000))
      gmt.asInstanceOf[Ext].toLocaleDateString(locale, js.undefined)
end Timestamp
