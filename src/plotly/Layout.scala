package plotly

import scala.scalajs.js

import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialLegendBgcolor
import typings.plotlyJs.anon.PartialMargin
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.right

import core.metrics.*

trait Layout[A] extends (A => PartialLayout):
  extension (a: A) inline def layout = this(a)
end Layout
object Layout:
  given common: Layout[Common] = _ =>
    PartialLayout()
      .setHeight(200)
      .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

  given intervals(using Layout[Common], Title[Intervals]): Layout[Intervals] = is =>
    inline def inside = PartialLegendBgcolor()
      .setX(1.05)
      .setY(0.5)
      .setItemclick(`false`)
      .setItemdoubleclick(`false`)

    Common.layout
      .setTitle(is.title)
      .setShowlegend(true)
      .setLegend(inside)
      .setXaxis(PartialLayoutAxis().setDtick(1.0).setTitle("圈数"))
      .setYaxis(PartialLayoutAxis().setOverlaying("y2").setTickmodeSync)
      .setYaxis2(PartialLayoutAxis().setSide(right))
  end intervals

  given history(using Layout[Common], Title[History]): Layout[History] = h =>
    Common.layout.setTitle(h.title).setShowlegend(false)

  extension (a: PartialLayoutAxis)
    private inline def setTickmodeSync: PartialLayoutAxis =
      a.asInstanceOf[js.Dynamic].tickmode = "sync"
      a

end Layout
