package plotly

import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJs.plotlyJsStrings.plotly_unhover
import typings.std.stdStrings.highlight
import typings.std.stdStrings.normal
import typings.std.stdStrings.visible

import core.metrics.*

type Context[A] = (A, PlotlyHTMLElement)
trait Listen[A, B] extends (Context[A] => Unit)
object Listen:
  given empty[A]: Listen[A, EmptyTuple] = (_, _) => ()

  given tuple[A, H, T <: Tuple](using h: Listen[A, H], t: Listen[A, T]): Listen[A, H *: T] = (a, p) =>
    h(a, p); t(a, p)

  given legendclick(
    using
    show: Restyle[visible],
    hidden: Restyle[legendonly]
  ): Listen[Intervals, plotly_legendclick] = (_, p) =>
    p.on(
        plotly_legendclick,
        accept[LegendClickEvent]: e =>
          org.scalajs.dom.console.log(e)
          e.curveNumber.toInt match
            case 0 =>
            case i =>
              val (l, r) = Range(1, e.data.length).partition(_ == i)
              show(p, l)
              hidden(p, r)
    )
  end legendclick

  given hover(using h: Restyle[highlight], n: Restyle[normal]): Listen[History, plotly_hover] = (_, p) =>
    val area: HTMLElement = p.querySelector(".nsewdrag")
    p.on_plotlyhover(
        plotly_hover,
        e =>
          area.style.cursor = "pointer"
          h(p, Seq(e.points(0).curveNumber.intValue))
    )
    p.on(
      plotly_unhover, 
      accept[PlotMouseEvent]: e => 
        area.style.cursor = ""
        n(p, Seq(e.points(0).curveNumber.intValue))
    )
  end hover

end Listen
