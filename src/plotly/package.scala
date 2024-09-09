package plotly

import scala.scalajs.js

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.MouseEvent

import typings.plotlyJs.mod.ModeBarButton
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.plotly_click
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJsDistMin.mod.Icons

import core.Inject
import core.metrics.*

type Trend[A] = (Intervals, A)

sealed trait Common
object Common extends Common

private[plotly] inline def accept[A](f: A => Unit): js.Any => Unit = a => f(a.asInstanceOf[A])

private[plotly] given Conversion[PlotlyHTMLElement, HTMLElement] = _.asInstanceOf[HTMLElement]

given history(
  using Render[History],
  Correlate[Intervals],
  Config[Trend[ModeBarButton]],
  Listen[Intervals, plotly_legendclick],
  Listen[History, plotly_hover]
): Inject[History] =
  case (r, h) =>
    inline def back                = ModeBarButton((_, _) => history(r -> h), Icons.home, "back", "回退")
    given Config[Intervals]        = i => (i -> back).config
    given DataArrayFrom[Intervals] = Correlate[Intervals].data(1)
    given Layout[Intervals]        = Correlate[Intervals].layout(1)
    given Render[Intervals]        = Render[Intervals]

    h.render(r)
      .`then`: p =>
        summon[Listen[History, plotly_hover]](h, p)
        p.on(
            plotly_click,
            accept[PlotMouseEvent]: e =>
              val i  = e.points(0).curveNumber.intValue
              val is = h(i)
              is.render(r).`then`(summon[Listen[Intervals, plotly_legendclick]](is, _))
        )

end history
