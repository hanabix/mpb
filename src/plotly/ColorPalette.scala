package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialLayout

trait ColorPalette[A]:
  def list: List[String]
  extension (i: Int) inline def color                     = list(i % list.length)
  extension (a: PartialLayout) inline def setColorPalette = a.setColorway(list.toJSArray)
end ColorPalette

object ColorPalette:
  given  ColorPalette[Common] with
    val list: List[String] = List(
        "#1f77b4",
        "#ff7f0e",
        "#2ca02c",
        "#d62728",
        "#9467bd",
        "#8c564b",
        "#e377c2",
        "#7f7f7f",
        "#bcbd22",
        "#17becf"
    )
  end given
end ColorPalette
