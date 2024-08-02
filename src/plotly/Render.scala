package plotly

import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJsDistMin.mod.newPlot

import core.Injection

trait Render[A] extends (Injection[A] => js.Promise[PlotlyHTMLElement]):
  extension (a: A) inline def render(r: HTMLElement) = this(r -> a)

object Render:
  def apply[A: DataArrayFrom: Layout: Config]: Render[A]    = (e, a) => newPlot(e, a.darr, a.layout, a.config)
  given render[A: DataArrayFrom: Layout: Config]: Render[A] = apply[A]
end Render
