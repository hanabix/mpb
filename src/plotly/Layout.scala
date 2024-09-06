package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialMargin

import core.metrics.*

trait Layout[A] extends (A => PartialLayout):
  extension (a: A) inline def layout = this(a)
end Layout
object Layout:
  given common(using ColorPalette[Common]): Layout[Common] = _ =>
    PartialLayout().setColorPalette
      .setHeight(200)
      .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

  given [A: Title](using Layout[Common]): Layout[A] = a => Common.layout.setTitle(a.title)


  given history(using Layout[Common], Title[History], ColorPalette[Common]): Layout[History] = h =>
    Common.layout
      .setTitle(h.title)
      .setShowlegend(false)
      .setColorPalette
      .setYaxis(PartialLayoutAxis().setTickformat(".2f").setColor(0.color))
end Layout
