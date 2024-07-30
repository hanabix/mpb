package plot

import scala.scalajs.js

import org.scalajs.dom.*

import typings.plotlyJs.anon.*
import typings.plotlyJs.mod.*
import typings.plotlyJsDistMin.mod

object Plot:
  def apply[A](root: HTMLElement, data: js.Array[A], title: String)(using
    Conversion[A, Data]
  ): js.Promise[PlotlyHTMLElement] =
    apply(root, data, Layout(title), Config())

  def apply[A](root: HTMLElement, data: js.Array[A], layout: PartialLayout, config: PartialConfig = Config())(using
    Conversion[A, Data]
  ): js.Promise[PlotlyHTMLElement] =
    mod.newPlot(root, data.map(_.convert), layout, config)

  object Layout:
    def apply(title: String) = PartialLayout()
      .setTitle(title)
      .setHeight(200)
      .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

  object Config:
    def apply() = PartialConfig().setResponsive(true)
end Plot
