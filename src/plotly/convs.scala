package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.mod.PlotlyHTMLElement

private[plotly] object convs:
  inline def accept[A](f: A => Unit): js.Any => Unit  = a => f(a.asInstanceOf[A])
  given Conversion[PlotlyHTMLElement, HTMLElement]    = _.asInstanceOf[HTMLElement]
  given [A]: Conversion[IterableOnce[A], js.Array[A]] = _.toJSArray
