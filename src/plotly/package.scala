package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.*
import typings.plotlyJs.mod.{Axis as _, Icons as _, *}
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.*
import typings.plotlyJsDistMin.mod.Icons
import typings.plotlyJsDistMin.mod.newPlot
import typings.plotlyJsDistMin.mod.react

import convs.given
import core.*

private type Handler[A] = (PlotlyHTMLElement, A) => Unit

type Ancestry[A] = (History[A], () => Unit)

given Conversion[LegendClickEvent, Double]         = _.curveNumber
given [A <: PlotMouseEvent]: Conversion[A, Double] = _.points(0).curveNumber

given PartialLegendBgcolor = PartialLegendBgcolor()
  .setX(1.1)
  .setY(0.5)
  .setItemclick(`false`)
  .setItemdoubleclick(`false`)

given (using ColorPalette): PartialLayout = PartialLayout().setColorPalette
  .setHeight(200)
  .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

given PartialConfig = PartialConfig()
  .setResponsive(true)
  .setDisplayModeBar(true)
  .setModeBarButtonsToRemoveVarargs(
    ModeBarDefaultButtons.lasso2d,
    ModeBarDefaultButtons.select2d,
    ModeBarDefaultButtons.pan2d,
    ModeBarDefaultButtons.zoom2d,
    ModeBarDefaultButtons.zoomIn2d,
    ModeBarDefaultButtons.zoomOut2d,
    ModeBarDefaultButtons.autoScale2d,
    ModeBarDefaultButtons.resetScale2d
  )

given [A](using
  Conversion[PlotMouseEvent, Double],
  PartialConfig,
  CorrelatePlot[Interval[A]],
  Registry[History[A], PlotMouseEvent],
  Listen[plotly_legendclick, Interval[A]]
): Registry[Ancestry[A], PlotMouseEvent] with
  extension (a: Ancestry[A])
    def handler: Handler[PlotMouseEvent] = (p, e) =>
      val (ha @ (h, t), back) = a: @unchecked
      ha.handler(p, e)

      val conf = summon[PartialConfig].setModeBarButtonsToAddVarargs:
        ModeBarButton((_, _) => back(), Icons.home, "back", "回退")

      val ia = (h :: t)(e.convert.intValue)
      val select = ia.plot: (data, layout) =>
        newPlot(p, data, layout, conf).`then`: pp =>
          Listen[plotly_legendclick, Interval[A]](pp, ia)

      select(1)

end given

given [A](using
  PartialConfig,
  PartialLayout,
  Trace[Box, History[A]],
  Axis[Box],
  Title[History[A]],
  Listen[(plotly_hover, plotly_unhover, plotly_click), History[A]],
  Listen[plotly_click, Ancestry[A]]
): Inject[History[A]] with
  extension (h: History[A])
    def inject(e: HTMLElement): Unit =

      val layout = summon[PartialLayout]
        .setTitle(Title[History[A]](h))
        .setShowlegend(false)
        .setYaxis(Axis[Box].head)

      val config = summon[PartialConfig]
      val data   = Trace[Box, History[A]](h)

      newPlot(e, data, layout, config).`then`: p =>
        def back(): Unit =
          react(p, data, layout, config.setModeBarButtonsToAddVarargs()).`then`: _ =>
            Listen[(plotly_hover, plotly_unhover, plotly_click), History[A]](p, h)
            Listen[plotly_click, Ancestry[A]](p, h -> back)

        Listen[(plotly_hover, plotly_unhover, plotly_click), History[A]](p, h)
        Listen[plotly_click, Ancestry[A]](p, h -> back)

end given
