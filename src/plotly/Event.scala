package plotly

import typings.plotlyJs.mod.*
import typings.plotlyJs.plotlyJsStrings.*

trait Event[A]:
  type Data
  def instance: A
object Event:

  given Event[plotly_click] with
    type Data = PlotMouseEvent
    def instance = plotly_click

  given Event[plotly_hover] with
    type Data = PlotHoverEvent
    def instance = plotly_hover

  given Event[plotly_unhover] with
    type Data = PlotMouseEvent
    def instance = plotly_unhover

  given Event[plotly_legendclick] with
    type Data = LegendClickEvent
    def instance = plotly_legendclick
end Event
