package plotly

import typings.plotlyJs.anon.PartialConfig
import typings.plotlyJs.mod.ModeBarButton
import typings.plotlyJs.mod.ModeBarDefaultButtons

import core.metrics.*

trait Config[A] extends (A => PartialConfig):
  extension (a: A) inline def config = this(a)

object Config:
  given common: Config[Common] = _ =>
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
  end common

  given line(using Config[Common]): Config[Trend[ModeBarButton]] = (_, b) =>
    Common.config.setModeBarButtonsToAddVarargs(b)
  end line

  given history(using Config[Common]): Config[History] = _ => Common.config.setModeBarButtonsToAddVarargs()
end Config
