package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJs.plotlyJsStrings.plotly_unhover
import typings.plotlyJsDistMin.mod.restyle
import typings.plotlyJsDistMin.mod.react

import core.metrics.*

type Context[A] = (A, PlotlyHTMLElement)
trait Listen[A, B] extends (Context[A] => Unit)
object Listen:
  given empty[A]: Listen[A, EmptyTuple] = (_, _) => ()

  given tuple[A, H, T <: Tuple](using h: Listen[A, H], t: Listen[A, T]): Listen[A, H *: T] = (a, p) =>
    h(a, p); t(a, p)

  given legendclick(using ColorPalette[Common], Select[Intervals]): Listen[Intervals, plotly_legendclick] = (i, p) =>
    p.on(
        plotly_legendclick,
        accept[LegendClickEvent]: e =>
          e.curveNumber.toInt match
            case 0 =>
            case n => 
              given DataArrayFrom[Intervals] = Select[Intervals].data(n)
              given Layout[Intervals] = Select[Intervals].layout(n)
              react(p, i.darr, i.layout)
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
