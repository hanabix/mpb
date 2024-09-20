package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import typings.plotlyJs.mod.PlotlyHTMLElement
import typings.plotlyJsDistMin.mod.*

import convs.given

trait Registry[A, B]:
  extension (a: A) def handler: Handler[B]
object Registry:
  given refresh[A, B](using Style[A, B], Conversion[A, Double]): Registry[B, A] with
    extension (b: B) def handler: Handler[A] = (p, a) => restyle(p, b.style(p), a.convert)

  given correlate[A, B](using CorrelatePlot[A], Conversion[B, Double]): Registry[A, B] with
    extension (a: A) def handler: Handler[B] = (p, b) => a.plot(react(p, _, _))(b.convert.intValue)

end Registry
