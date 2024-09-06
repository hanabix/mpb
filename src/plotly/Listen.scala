package plotly

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJs.plotlyJsStrings.plotly_unhover
import typings.plotlyJs.plotlyJsStrings.right
import typings.plotlyJsDistMin.mod.relayout
import typings.plotlyJsDistMin.mod.restyle

import core.metrics.*

type Context[A] = (A, PlotlyHTMLElement)
trait Listen[A, B] extends (Context[A] => Unit)
object Listen:
  given empty[A]: Listen[A, EmptyTuple] = (_, _) => ()

  given tuple[A, H, T <: Tuple](using h: Listen[A, H], t: Listen[A, T]): Listen[A, H *: T] = (a, p) =>
    h(a, p); t(a, p)

  given legendclick(using ColorPalette[Common]): Listen[Intervals, plotly_legendclick] = (_, p) =>
    p.on(
        plotly_legendclick,
        accept[LegendClickEvent]: e =>
          e.curveNumber.toInt match
            case 0 =>
            case i =>
              val (l, r) = Range(1, e.data.length).partition(_ == i)
              restyle(p, PartialPlotDataAutobinx().setVisible(true), l.map(_.doubleValue).toJSArray)
              restyle(p, PartialPlotDataAutobinx().setVisible(legendonly), r.map(_.doubleValue).toJSArray)
              val yAxis2 = PartialLayoutAxis().setColor(i.color).setSide(right)
              relayout(p, PartialLayout().setYaxis2(yAxis2))
          end match
    )
  end legendclick

  given hover: Listen[History, plotly_hover] = (_, p) =>
    val area: HTMLElement = p.querySelector(".nsewdrag")
    p.on_plotlyhover(
        plotly_hover,
        e =>
          area.style.cursor = "pointer"
          val data = PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(2))
          restyle(p, data, e.points(0).curveNumber)
    )
    p.on(
        plotly_unhover,
        accept[PlotMouseEvent]: e =>
          area.style.cursor = ""
          val data = PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(1))
          restyle(p, data, e.points(0).curveNumber)
    )
  end hover

end Listen
