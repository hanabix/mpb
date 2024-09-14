package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import typings.plotlyJs.anon.PartialConfig
import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLegendBgcolor
import typings.plotlyJs.anon.PartialMargin
import typings.plotlyJs.mod.ModeBarDefaultButtons
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.plotly_click
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJsDistMin.mod.newPlot
import typings.plotlyJsDistMin.mod.react

import convs.given
import core.*

type Ancestry[A] = (History[A], () => Unit)

given PartialLegendBgcolor = PartialLegendBgcolor()
  .setX(1.1)
  .setY(0.5)
  .setItemclick(`false`)
  .setItemdoubleclick(`false`)

given (using ColorPalette): PartialLayout = PartialLayout().setColorPalette
  .setHeight(200)
  .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

given PartialConfig =
  PartialConfig()
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

given history[A](using
  PartialConfig,
  PartialLayout,
  Trace[Box, History[A]],
  Axis[Box],
  Title[History[A]],
  Listen[(plotly_click, plotly_hover), Ancestry[A]]
): Inject[History[A]] = (r, h) =>

  val layout = summon[PartialLayout]
    .setTitle(Title[History[A]](h))
    .setShowlegend(false)
    .setYaxis(Axis[Box].head)

  val config = summon[PartialConfig]
  val data   = Trace[Box, History[A]](h)

  newPlot(r, data, layout, config).`then`: p =>
    def back(): Unit = react(r, data, layout, config.setModeBarButtonsToAddVarargs())
    Listen[(plotly_click, plotly_hover), Ancestry[A]](h -> back, p)

end history
