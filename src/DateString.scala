import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialBoxPlotData
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings.*

object DateString:
  extension (v: String | Double)
    inline def ymd(locale: String): String =
      date(v).asInstanceOf[js.Dynamic & Ext].toLocaleDateString(locale, js.undefined)

    inline def md(locale: String): String =
      val opt = js.Dynamic.literal(month = "2-digit", day = "2-digit")
      date(v).asInstanceOf[js.Dynamic & Ext].toLocaleDateString(locale, opt)

  private inline def date(v: String | Double) = inline v match
    case s: String => new js.Date(s)
    case d: Double => new js.Date(d)

  private type Ext = {
    def toLocaleDateString(locale: String, option: js.Any): String
  }

end DateString
