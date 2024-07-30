package common

import scala.scalajs.js

trait DateFormat[A] extends (A => js.Date):
  private type Ext = {
    def toLocaleDateString(locale: String, option: js.Any): String
  }

  extension (a: A)
    inline def ymd(locale: String) =
      this(a).asInstanceOf[Cast[Ext]].toLocaleDateString(locale, js.undefined)
    inline def md(locale: String) =
      val opt = js.Dynamic.literal(month = "2-digit", day = "2-digit")
      this(a).asInstanceOf[Cast[Ext]].toLocaleDateString(locale, opt)
end DateFormat

object DateFormat:
  given double: DateFormat[Double] = new js.Date(_)
  given string: DateFormat[String] = new js.Date(_)
