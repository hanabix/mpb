package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.Data
import typings.plotlyJs.mod.PlotType
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJs.plotlyJsStrings.y
import core.DateFormat
import core.metrics.*

trait ColorPalette[A]:
  def list: List[String]
  extension (i: Int) inline def color = list(i % list.length)
end ColorPalette

object ColorPalette:
  given ColorPalette[Intervals] with
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