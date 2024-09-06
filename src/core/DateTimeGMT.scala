package core

import scala.scalajs.js

trait DateTimeGMT[A] extends (A => js.Date):
  private trait Ext extends js.Object:
    def toLocaleDateString(locale: String, option: js.Any): String

  extension (a: A)
    inline def ymd(locale: String) =
      this(a).asInstanceOf[Ext].toLocaleDateString(locale, js.undefined)
      
  end extension
end DateTimeGMT

object DateTimeGMT:
  given string: DateTimeGMT[String] = s => 
    val d = new js.Date(s)
    new js.Date(d.getTime() - (d.getTimezoneOffset() * 60000))
end DateTimeGMT
