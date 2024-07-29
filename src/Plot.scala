import scala.scalajs.js.*

import org.scalajs.dom.*

import typings.plotlyJs.anon.*
import typings.plotlyJs.mod.*
import typings.plotlyJsDistMin.mod

object Plot:
  def apply[A](root: HTMLElement, data: Array[A], title: String)(using Conversion[A, Data]) =
    mod.newPlot(root, data.map(_.convert), Layout(title), Config())

  object Layout:
    def apply(title: String) = PartialLayout()
      .setTitle(title)
      .setHeight(200)
      .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

  object Config:
    def apply() = PartialConfig().setResponsive(true)
end Plot
