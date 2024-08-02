package core

import scala.scalajs.js

trait DateFormat[A] extends (A => js.Date):
  private trait Ext extends js.Object:
    def toLocaleDateString(locale: String, option: js.Any): String

  extension (a: A)
    inline def ymd(locale: String) =
      this(a).asInstanceOf[Ext].toLocaleDateString(locale, js.undefined)
      
    inline def md(locale: String) =
      val opt = js.Dynamic.literal(month = "2-digit", day = "2-digit")
      this(a).asInstanceOf[Ext].toLocaleDateString(locale, opt)
    end md
  end extension
end DateFormat

object DateFormat:
  given double: DateFormat[Double] = new js.Date(_)
  given string: DateFormat[String] = new js.Date(_)
end DateFormat
