package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJsDistMin.mod.restyle
import typings.std.stdStrings.highlight
import typings.std.stdStrings.normal
import typings.std.stdStrings.visible

trait Restyle[A] extends ((PlotlyHTMLElement, Seq[Int]) => Unit)
object Restyle:

  given Restyle[visible] = (e, ts) =>
    restyle(e, PartialPlotDataAutobinx().setVisible(true), ts.map(_.doubleValue).toJSArray)

  given Restyle[legendonly] = (e, ts) =>
    restyle(e, PartialPlotDataAutobinx().setVisible(legendonly), ts.map(_.doubleValue).toJSArray)

  given Restyle[highlight] = (e, ts) =>
    restyle(e, PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(2)), ts.map(_.doubleValue).toJSArray)

  given Restyle[normal] = (e, ts) =>
    restyle(e, PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(1)), ts.map(_.doubleValue).toJSArray)

end Restyle
