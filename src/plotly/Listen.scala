package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.PartialConfig
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.ModeBarButton
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.plotly_click
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJs.plotlyJsStrings.plotly_unhover
import typings.plotlyJsDistMin.mod.Icons
import typings.plotlyJsDistMin.mod.newPlot
import typings.plotlyJsDistMin.mod.react
import typings.plotlyJsDistMin.mod.restyle

import convs.{*, given}
import core.*

opaque type Listen[A, B] = (A, PlotlyHTMLElement) => Unit
object Listen:
  def apply[A, B](a: A, p: PlotlyHTMLElement)(using l: Listen[A, B]) = l(a, p)

  given empty[A]: Listen[A, EmptyTuple] = (_, _) => ()
  given tuple[A, H, T <: Tuple](using h: Listen[A, H], t: Listen[A, T]): Listen[A, H *: T] = (a, p) =>
    h(a, p); t(a, p)

  given [A](using CorrelatePlot[Interval[A]]): Listen[Interval[A], plotly_legendclick] = (is, p) =>
    val select = is.plot(react(p, _, _))
    p.on(
      plotly_legendclick,
      accept[LegendClickEvent]: e =>
        select(e.curveNumber.toInt)
    )
  end given

  given [A]: Listen[A, plotly_hover] = (_, p) =>
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
  end given

  given [A](using
    PartialConfig,
    Listen[Interval[A], plotly_legendclick],
    CorrelatePlot[Interval[A]]
  ): Listen[Ancestry[A], plotly_click] =
    case (((h, t), back), p) =>
      val conf = summon[PartialConfig].setModeBarButtonsToAddVarargs:
        ModeBarButton((_, _) => back(), Icons.home, "back", "回退")

      p.on(
        plotly_click,
        accept[PlotMouseEvent]: e =>
          val curve = e.points(0).curveNumber
          restyle(p, PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(1)), curve)

          val ia = (h :: t)(curve.intValue)
          val select = ia.plot: (data, layout) =>
            newPlot(p, data, layout, conf).`then`(Listen[Interval[A], plotly_legendclick](ia, _))
          select(1)
      )
  end given

end Listen
