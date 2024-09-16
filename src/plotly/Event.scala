package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.PartialConfig
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.LegendClickEvent
import typings.plotlyJs.mod.ModeBarButton
import typings.plotlyJs.mod.PlotHoverEvent
import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJs.mod.PlotMouseEvent
import typings.plotlyJs.plotlyJsStrings.plotly_click
import typings.plotlyJs.plotlyJsStrings.plotly_hover
import typings.plotlyJs.plotlyJsStrings.plotly_legendclick
import typings.plotlyJs.plotlyJsStrings.plotly_unhover

import convs.{*, given}
import core.*

trait Event[A]:
  type Data
  def instance: A
object Event:
  def apply[A](using e: Event[A]) = e

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