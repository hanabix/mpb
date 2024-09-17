package plotly

import scala.scalajs.js

import org.scalajs.dom.Element
import org.scalajs.dom.HTMLElement

import typings.plotlyJs.anon.*
import typings.plotlyJs.mod.*

import core.*

trait Style[A, B]:
  extension (a: A) def style(e: HTMLElement): Data
object Style:
  given [A]: Style[History[A], PlotHoverEvent] with
    extension (a: History[A])
      def style(e: HTMLElement): Data =
        e.querySelector(".nsewdrag").cursor("pointer")
        DataLineWidth(2)

  given [A]: Style[History[A], PlotMouseEvent] with
    extension (a: History[A])
      def style(e: HTMLElement): Data =
        e.querySelector(".nsewdrag").cursor("")
        DataLineWidth(1)

  private inline def DataLineWidth(n: Int) =
    Data.PartialPlotDataAutobinx().setLine(PartialScatterLine().setWidth(n))

  extension (e: Element) private inline def cursor(value: String): Unit = 
    e.asInstanceOf[HTMLElement].style.cursor = value
end Style
