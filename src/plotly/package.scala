package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import typings.plotlyJs.anon.PartialConfig
import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLegendBgcolor
import typings.plotlyJs.anon.PartialMargin
import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.ModeBarButton
import typings.plotlyJs.mod.ModeBarDefaultButtons
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.plotly_click
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_unhover
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
  Registry[History[A], PlotMouseEvent]
): Registry[Ancestry[A], PlotMouseEvent] with
  extension (a: Ancestry[A])
    def handler: Handler[PlotMouseEvent] = (p, e) =>
      val (ha @ (h, t), back) = a: @unchecked
      ha.handler(p, e)

      val conf = summon[PartialConfig].setModeBarButtonsToAddVarargs:
        ModeBarButton((_, _) => back(), Icons.home, "back", "回退")

      val select = (h :: t)(e.convert.intValue).plot: (data, layout) =>
        newPlot(p, data, layout, conf)

      select(1)
      
end given

given [A](using
  PartialConfig,
  PartialLayout,
  Trace[Box, History[A]],
  Axis[Box],
  Title[History[A]],
  Listen[(plotly_hover, plotly_unhover), History[A]],
  Listen[plotly_click, Ancestry[A]]
): Inject[History[A]] = (r, h) =>

  val layout = summon[PartialLayout]
    .setTitle(Title[History[A]](h))
    .setShowlegend(false)
    .setYaxis(Axis[Box].head)

  val config = summon[PartialConfig]
  val data   = Trace[Box, History[A]](h)

  newPlot(r, data, layout, config).`then`: p =>
    def back(): Unit = react(r, data, layout, config.setModeBarButtonsToAddVarargs())
    Listen[(plotly_hover, plotly_unhover), History[A]](p, h)
    Listen[plotly_click, Ancestry[A]](p, h -> back)

end given
