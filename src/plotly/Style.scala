package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.Data
import typings.plotlyJs.mod.PlotHoverEvent
import typings.plotlyJs.mod.PlotMouseEvent

import core.*

trait Style[A, B]:
  extension (a: A) def style(e: HTMLElement): Data
object Style:
  given [A]: Style[History[A], PlotHoverEvent] with
    extension (a: History[A])
      def style(e: HTMLElement): Data =
        e.querySelector(".nsewdrag").asInstanceOf[HTMLElement].style.cursor = "pointer"
        Data.PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(2))
        
  given [A]: Style[History[A], PlotMouseEvent] with
    extension (a: History[A])
      def style(e: HTMLElement): Data =
        e.querySelector(".nsewdrag").asInstanceOf[HTMLElement].style.cursor = ""
        Data.PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(1))
end Style
