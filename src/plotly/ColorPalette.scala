package plotly

import scala.language.implicitConversions

import typings.plotlyJs.anon.*

import convs.given

final class ColorPalette(list: List[String]):
  extension (a: PartialLayoutAxis) inline def setColorIndex(i: Int) = a.setColor(list(i % list.length))
  extension (a: PartialLayout) inline def setColorPalette           = a.setColorway(list)

object ColorPalette:
  given ColorPalette = ColorPalette:
    List(
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
